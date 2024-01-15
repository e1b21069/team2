//AsyncPlayMatch.java

package oit.is.work.team2.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;

import oit.is.work.team2.model.Dictionary;
import oit.is.work.team2.model.DictionaryMapper;
import oit.is.work.team2.model.MatchMapper;
import oit.is.work.team2.model.UserMapper;
import oit.is.work.team2.model.WordLog;
import oit.is.work.team2.model.WordLogMapper;
import oit.is.work.team2.model.WordleLog;
import oit.is.work.team2.model.WordleLogMapper;

@Service
public class AsyncPlayMatch {
  private final SseEmitter emitter = new SseEmitter();

  private volatile boolean[] dbUpdated = {false, false, false, false, false, false};
  private volatile boolean[] wdbUpdated = {false, false, false, false, false, false};
  private volatile boolean[] secondUpdated = {false, false, false, false, false, false};
  private volatile boolean dbUpdated2 = false;
  private volatile boolean wdbUpdated2 = false;
  private volatile boolean secondUpdated2 = false;
  private final Logger logger = LoggerFactory.getLogger(AsyncPlayMatch.class);

  @Autowired
  WordLogMapper wordLogMapper;
  
  @Autowired
  private DictionaryMapper dictionaryMapper;

  @Autowired
  MatchMapper matchMapper;

  @Autowired
  private UserMapper usermapper;
  
  WordleLogMapper wordleLogMapper;


  public SseEmitter getEmitter() {
    return emitter;
  }

  public void sendData(String data) {
    try {
      emitter.send(SseEmitter.event().data(data));
    } catch (IOException e) {
      emitter.completeWithError(e);
    }
  }

  // Numeron用

  @Transactional
  public void syncAddWordLogs(int roomId, String ans, int eatcnt, int bitecnt) {
    // 追加
    wordLogMapper.insertMulti(roomId, ans, eatcnt, bitecnt);
    // 非同期でDB更新したことを共有する際に利用する
    this.dbUpdated[roomId] = true;
    this.wdbUpdated[roomId] = true;
  }

  public ArrayList<WordLog> syncShowWordLogList(int roomId) {
    return wordLogMapper.selectAllByRoomId(roomId);
  }

  @Async
  public void asyncShowWordLogsList(SseEmitter emitter, int roomId) {
    int count = 0;
    try {
      while (true) {// 無限ループ
        // DBが更新されていなければ0.1s休み
        if (false == dbUpdated[roomId]) {
          if (count % 100 == 0) {
            Map<String, String> data = new HashMap<>();
            data.put("type", "msg");
            data.put("message", "dontMove");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);
            emitter.send(jsonData);
          }
          count++;
          TimeUnit.MILLISECONDS.sleep(100);
          continue;
        }
        // DBが更新されていれば更新後の単語リストを取得してsendし，1s休み，dbUpdatedをfalseにする
        // emitter.send(true);
        String ans = wordLogMapper.selectAns();
        String word = matchMapper.selectWord(roomId);

        if (ans.equals(word)) {
          Map<String, String> data = new HashMap<>();
          data.put("type", "msg");
          data.put("message", "loserScreen");
          ObjectMapper objectMapper = new ObjectMapper();
          String jsonData = objectMapper.writeValueAsString(data);
          emitter.send(jsonData);
        } else {
          Map<String, String> data = new HashMap<>();
          data.put("type", "msg");
          data.put("message", "nextScreen");
          // Jackson ObjectMapperを使用してJSON形式の文字列に変換
          ObjectMapper objectMapper = new ObjectMapper();
          String jsonData = objectMapper.writeValueAsString(data);
          emitter.send(jsonData);
        }
        dbUpdated[roomId] = false;
        secondUpdated[roomId] = true;
      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowDictionariesList complete");
  }

  @Async
  public void asyncUpdate(SseEmitter emitter, int roomId) {
    wdbUpdated[roomId] = true;
    try {
      while (true) {// 無限ループ
        ArrayList<WordLog> wordLogs = wordLogMapper.selectAllByRoomId(roomId);
        emitter.send(wordLogs);
        wdbUpdated[roomId] = false;
        TimeUnit.MILLISECONDS.sleep(300);
      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowDictionariesList complete");
  }

  @Async
  public void asyncShowSecond(SseEmitter emitter, int roomId) {
    int count = 0;
    try {
      while (true) {// 無限ループ
        // DBが更新されていなければ0.5s休み
        if (false == secondUpdated[roomId]) {
          if (count % 100 == 0) {
            Map<String, String> data = new HashMap<>();
            data.put("type", "msg");
            data.put("message", "dontMove");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);
            emitter.send(jsonData);
          }
          count++;
          TimeUnit.MILLISECONDS.sleep(100);
          continue;
        }
        if (false == dbUpdated[roomId]) {
          if (count % 100 == 0) {
            Map<String, String> data = new HashMap<>();
            data.put("type", "msg");
            data.put("message", "dontMove");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);
            emitter.send(jsonData);
          }
          count++;
          TimeUnit.MILLISECONDS.sleep(100);
          continue;
        }
        // DBが更新されていれば更新後の単語リストを取得してsendし，1s休み，dbUpdatedをfalseにする
        // emitter.send(true);
        String ans = wordLogMapper.selectAns();
        String word = matchMapper.selectWord(roomId);

        if (ans.equals(word)) {
          Map<String, String> data = new HashMap<>();
          data.put("type", "msg");
          data.put("message", "loserScreen");
          ObjectMapper objectMapper = new ObjectMapper();
          String jsonData = objectMapper.writeValueAsString(data);
          emitter.send(jsonData);
        } else {
          Map<String, String> data = new HashMap<>();
          data.put("type", "msg");
          data.put("message", "nextScreen");
          // Jackson ObjectMapperを使用してJSON形式の文字列に変換
          ObjectMapper objectMapper = new ObjectMapper();
          String jsonData = objectMapper.writeValueAsString(data);
          emitter.send(jsonData);
        }
        secondUpdated[roomId] = false;
        dbUpdated[roomId] = false;
      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowDictionariesList complete");
  }

  @Async
  public void asyncShowRoom(SseEmitter emitter, int roomId) {
    int count = 0;
    int pplNum = usermapper.selectCountByRoomId(roomId);
    try {
      while (true) {// 無限ループ
        // DBが更新されていなければ0.5s休み
        pplNum = usermapper.selectCountByRoomId(roomId);
        if (pplNum < 2) {
          if(count % 100 == 0) {
            Map<String, String> data = new HashMap<>();
            data.put("type", "msg");
            data.put("message", "dontMove");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);
            emitter.send(jsonData);
          }
          count++;
          TimeUnit.MILLISECONDS.sleep(300);
          continue;
        }
          Map<String, String> data = new HashMap<>();
          data.put("type", "msg");
          data.put("message", "startMatch");
          // Jackson ObjectMapperを使用してJSON形式の文字列に変換
          ObjectMapper objectMapper = new ObjectMapper();
          String jsonData = objectMapper.writeValueAsString(data);
          emitter.send(jsonData);
          TimeUnit.MILLISECONDS.sleep(1000);
      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowRoom complete");
  }

  public String setupMatch(int roomId) {
    ArrayList<Dictionary> allWords = dictionaryMapper.selectAll();
    if (allWords.isEmpty())
      return "No words found in the database";

    int randomIndex = (int) (Math.random() * allWords.size());
    String randomWord = allWords.get(randomIndex).getWord();

    matchMapper.insert(roomId, randomWord);

    return randomWord;
  }


// Wordle用
  
  @Transactional
  public void syncAddWordleLogs(int roomId, String ans, int result) {
    // 追加
    wordleLogMapper.insertMulti(roomId, ans, result);
    // 非同期でDB更新したことを共有する際に利用する
    this.dbUpdated[roomId] = true;
    this.wdbUpdated[roomId] = true;
  }

  public ArrayList<WordleLog> syncShowWordleLogList(int roomId) {
    return wordleLogMapper.selectAllByRoomId(roomId);
  }

  @Async
  public void asyncShowWordleLogsList(SseEmitter emitter, int roomId) {
    int count = 0;
    try {
      while (true) {// 無限ループ
        // DBが更新されていなければ0.1s休み
        if (false == dbUpdated[roomId]) {
          if (count % 100 == 0) {
            Map<String, String> data = new HashMap<>();
            data.put("type", "msg");
            data.put("message", "dontMove");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);
            emitter.send(jsonData);
          }
          count++;
          TimeUnit.MILLISECONDS.sleep(100);
          continue;
        }
        // DBが更新されていれば更新後の単語リストを取得してsendし，1s休み，dbUpdatedをfalseにする
        // emitter.send(true);
        String ans = wordleLogMapper.selectAns();
        String word = matchMapper.selectWord(roomId);

        if (ans.equals(word)) {
          Map<String, String> data = new HashMap<>();
          data.put("type", "msg");
          data.put("message", "loserScreen");
          ObjectMapper objectMapper = new ObjectMapper();
          String jsonData = objectMapper.writeValueAsString(data);
          emitter.send(jsonData);
        } else {
          Map<String, String> data = new HashMap<>();
          data.put("type", "msg");
          data.put("message", "nextScreen");
          // Jackson ObjectMapperを使用してJSON形式の文字列に変換
          ObjectMapper objectMapper = new ObjectMapper();
          String jsonData = objectMapper.writeValueAsString(data);
          emitter.send(jsonData);
        }
        dbUpdated[roomId] = false;
        secondUpdated[roomId]= true;
      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowDictionariesList complete");
  }

  @Async
  public void asyncWordleUpdate(SseEmitter emitter, int roomId) {
    wdbUpdated[roomId] = true;
    try {
      while (true) {// 無限ループ
        ArrayList<WordleLog> wordleLogs = wordleLogMapper.selectAllByRoomId(roomId);
        emitter.send(wordleLogs);
        wdbUpdated[roomId] = false;
        TimeUnit.MILLISECONDS.sleep(300);
      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowDictionariesList complete");
  }

  @Async
  public void asyncWordleShowSecond(SseEmitter emitter, int roomId) {
    int count = 0;
    try {
      while (true) {// 無限ループ
        // DBが更新されていなければ0.5s休み
        if (false == secondUpdated[roomId]) {
          if (count % 100 == 0) {
            Map<String, String> data = new HashMap<>();
            data.put("type", "msg");
            data.put("message", "dontMove");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);
            emitter.send(jsonData);
          }
          count++;
          TimeUnit.MILLISECONDS.sleep(100);
          continue;
        }
        if (false == dbUpdated[roomId]) {
          if (count % 100 == 0) {
            Map<String, String> data = new HashMap<>();
            data.put("type", "msg");
            data.put("message", "dontMove");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);
            emitter.send(jsonData);
          }
          count++;
          TimeUnit.MILLISECONDS.sleep(100);
          continue;
        }
        // DBが更新されていれば更新後の単語リストを取得してsendし，1s休み，dbUpdatedをfalseにする
        // emitter.send(true);
        String ans = wordleLogMapper.selectAns();
        String word = matchMapper.selectWord(roomId);

        if (ans.equals(word)) {
          Map<String, String> data = new HashMap<>();
          data.put("type", "msg");
          data.put("message", "loserScreen");
          ObjectMapper objectMapper = new ObjectMapper();
          String jsonData = objectMapper.writeValueAsString(data);
          emitter.send(jsonData);
        } else {
          Map<String, String> data = new HashMap<>();
          data.put("type", "msg");
          data.put("message", "nextScreen");
          // Jackson ObjectMapperを使用してJSON形式の文字列に変換
          ObjectMapper objectMapper = new ObjectMapper();
          String jsonData = objectMapper.writeValueAsString(data);
          emitter.send(jsonData);
        }
        dbUpdated[roomId] = false;
      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowDictionariesList complete");
  }

  public String setupWordleMatch(int roomId) {
    ArrayList<Dictionary> allWords = dictionaryMapper.selectAll();
    if (allWords.isEmpty())
      return "No words found in the database";

    int randomIndex = (int) (Math.random() * allWords.size());
    String randomWord = allWords.get(randomIndex).getWord();

    matchMapper.insert(roomId, randomWord);

    return randomWord;
  }

}
