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

import oit.is.work.team2.model.DictionaryMapper;
import oit.is.work.team2.model.Dictionary;

@Service
public class AsyncAdminDictionaryService {
    private volatile boolean dbUpdated = false;

    private final Logger logger = LoggerFactory.getLogger(AsyncAdminDictionaryService.class);

    @Autowired
    DictionaryMapper dictionaryMapper;

    @Transactional
    public Dictionary syncDeleteDictionaries(int id) {
        // 削除対象の単語を取得
        Dictionary dictionary = dictionaryMapper.selectById(id);
        // 削除
        dictionaryMapper.deleteById(id);
        // 非同期でDB更新したことを共有する際に利用する
        this.dbUpdated = true;
        return dictionary;
    }

    @Transactional
    public Dictionary syncEditDictionaries(int id) {
        // 編集対象の単語を取得
        Dictionary dictionary = dictionaryMapper.selectById(id);
        // 編集
        dictionaryMapper.updateById(dictionary);
        // 非同期でDB更新したことを共有する際に利用する
        this.dbUpdated = true;
        return dictionary;
    }

    @Transactional
    public Dictionary syncUpdateDictionaries(Dictionary dictionary) {
        // 編集対象の単語を取得
        dictionaryMapper.updateById(dictionary);
        // 非同期でDB更新したことを共有する際に利用する
        this.dbUpdated = true;
        return dictionary;
    }

    @Transactional
    public void syncAddDictionaries(String word) {
        // 追加
        dictionaryMapper.insert(word);
        // 非同期でDB更新したことを共有する際に利用する
        this.dbUpdated = true;
    }

    public ArrayList<Dictionary> syncShowDictionariesList() {
        return dictionaryMapper.selectAll();
    }

    @Async
    public void asyncShowDictionariesList(SseEmitter emitter) {
        dbUpdated = true;
        try {
            while (true) {// 無限ループ
                // DBが更新されていなければ0.5s休み
                if (false == dbUpdated) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    continue;
                }
                // DBが更新されていれば更新後の単語リストを取得してsendし，1s休み，dbUpdatedをfalseにする
                ArrayList<Dictionary> dictionaries = this.syncShowDictionariesList();
                emitter.send(dictionaries);
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
