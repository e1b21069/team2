//AsyncPlayMatch.java

package oit.is.work.team2.service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.work.team2.model.WordLog;
import oit.is.work.team2.model.WordLogMapper;

@Service
public class AsyncPlayMatch {
  private volatile boolean dbUpdated = false;
  private final Logger logger = LoggerFactory.getLogger(AsyncPlayMatch.class);

  @Autowired
  WordLogMapper wordLogMapper;

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
    dbUpdated = true;
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
        TimeUnit.MILLISECONDS.sleep(1000);
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
}