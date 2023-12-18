//AsyncPlayMatch.java

package oit.is.work.team2.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.work.team2.model.Dictionary;
import oit.is.work.team2.model.DictionaryMapper;
import oit.is.work.team2.model.Match;
import oit.is.work.team2.model.MatchMapper;

import oit.is.work.team2.model.WordLog;
import oit.is.work.team2.model.WordLogMapper;

@Service
public class AsyncPlayMatch {
  private final SseEmitter emitter = new SseEmitter();

  private volatile boolean dbUpdated = false;
  private volatile boolean wdbUpdated = false;
  private final Logger logger = LoggerFactory.getLogger(AsyncPlayMatch.class);

  @Autowired
  WordLogMapper wordLogMapper;
  @Autowired
  private DictionaryMapper dictionaryMapper;

  @Autowired
  MatchMapper matchMapper;

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

  @Transactional
  public void syncAddWordLogs(String ans, int eatcnt, int bitecnt) {
    // 追加
    wordLogMapper.insert(ans, eatcnt, bitecnt);
    // 非同期でDB更新したことを共有する際に利用する
    this.dbUpdated = true;
  }

  public ArrayList<WordLog> syncShowWordLogList() {
    return wordLogMapper.selectAll();
  }

  @Async
  public void asyncShowWordLogsList(SseEmitter emitter) {
    try {
      while (true) {// 無限ループ
        // DBが更新されていなければ0.5s休み
        if (false == dbUpdated) {
          TimeUnit.MILLISECONDS.sleep(100);
          continue;
        }
        // DBが更新されていれば更新後の単語リストを取得してsendし，1s休み，dbUpdatedをfalseにする
        // emitter.send(true);
        String ans = wordLogMapper.selectAns();
        String word = matchMapeer.selectWord(1);
        if(ans.equals(word)) {
          Map<String, String> data = new HashMap<>();
          data.put(key:"type", value:"msg");
          data.put(key:"message", value:"loserScreen")
        } else {
          Map<String, String> data = new HashMap<>();
              data.put("type", "msg");
              data.put("message", "nextScreen");
          // Jackson ObjectMapperを使用してJSON形式の文字列に変換
          ObjectMapper objectMapper = new ObjectMapper();
          String jsonData = objectMapper.writeValueAsString(data);
          emitter.send(jsonData);
        }

        dbUpdated = false;
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
  public void asyncOrderWait(SseEmitter emitter) {
    try {
      while (true) {// 無限ループ
        // DBが更新されていなければ0.5s休み
        if (false == dbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        // DBが更新されていれば更新後の単語リストを取得してsendし，1s休み，dbUpdatedをfalseにする
        ArrayList<WordLog> wordLogs = this.syncShowWordLogList();
        emitter.send(wordLogs);
        emitter.send(dbUpdated);
        dbUpdated = false;
        wdbUpdated = true;
      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowDictionariesList complete");
  } 

  public String setupMatch() {
    ArrayList<Dictionary> allWords = dictionaryMapper.selectAll();
    if (allWords.isEmpty())
      return "No words found in the database";

    int randomIndex = (int) (Math.random() * allWords.size());
    String randomWord = allWords.get(randomIndex).getWord();

    matchMapper.insert(randomWord, 1, 2);

    return randomWord;
  }

  public void updateMatch() {
  }

  public boolean selectUpdate() {
    return wdbUpdated;
  }

  public void switchUpdate() {
    wdbUpdated = false;
  }
}
