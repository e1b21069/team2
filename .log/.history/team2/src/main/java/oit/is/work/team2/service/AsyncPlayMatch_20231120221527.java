package oit.is.work.team2.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.work.team2.model.WordLog;

@Service
public class AsyncPlayMatch {
    int count = 1;
    private final Logger logger = LoggerFactory.getLogger(AsyncPlayMatch.class);
    @Async
    
    public void pushFruit(SseEmitter emitter) throws IOException {
      logger.info("pushFruit start");
      try {
        while (true) {// 無限ループ
          logger.info("send:" + count);
          // sendによってcountがブラウザにpushされる
          emitter.send(count);
          count++;
          // 1秒STOP
          TimeUnit.SECONDS.sleep(1);
        }
      } catch (InterruptedException e) {
        // 例外の名前とメッセージだけ表示する
        logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
      }
    }
}
