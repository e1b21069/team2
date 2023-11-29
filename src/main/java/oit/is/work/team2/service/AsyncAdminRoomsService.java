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

import oit.is.work.team2.model.Room;
import oit.is.work.team2.model.RoomMapper;

@Service
public class AsyncAdminRoomsService {
    private volatile boolean dbUpdated = false;

    private final Logger logger = LoggerFactory.getLogger(AsyncAdminRoomsService.class);

    @Autowired
    RoomMapper roomMapper;

    @Transactional
    public Room syncDeleteRooms(int id) {
        // 削除対象の単語を取得
        Room Room = roomMapper.selectById(id);
        // 削除
        roomMapper.deleteById(id);
        // 非同期でDB更新したことを共有する際に利用する
        this.dbUpdated = true;
        return Room;
    }

    @Transactional
    public Room syncEditRooms(int id) {
        // 編集対象の単語を取得
        Room Room = roomMapper.selectById(id);
        // 編集
        roomMapper.updateById(Room);
        // 非同期でDB更新したことを共有する際に利用する
        this.dbUpdated = true;
        return Room;
    }

    @Transactional
    public Room syncUpdateRooms(Room Room) {
        // 編集対象の単語を取得
        roomMapper.updateById(Room);
        // 非同期でDB更新したことを共有する際に利用する
        this.dbUpdated = true;
        return Room;
    }

    @Transactional
    public void syncAddRooms(String word) {
        // 追加
        roomMapper.insert(word);
        // 非同期でDB更新したことを共有する際に利用する
        this.dbUpdated = true;
    }

    public ArrayList<Room> syncShowRoomsList() {
        return roomMapper.selectAll();
    }

    @Async
    public void asyncShowRoomsList(SseEmitter emitter) {
        dbUpdated = true;
        try {
            while (true) {// 無限ループ
                // DBが更新されていなければ0.5s休み
                if (false == dbUpdated) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    continue;
                }
                // DBが更新されていれば更新後の単語リストを取得してsendし，1s休み，dbUpdatedをfalseにする
                ArrayList<Room> rooms = this.syncShowRoomsList();
                emitter.send(rooms);
                TimeUnit.MILLISECONDS.sleep(1000);
                dbUpdated = false;
            }
        } catch (Exception e) {
            // 例外の名前とメッセージだけ表示する
            logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
        } finally {
            emitter.complete();
        }
        System.out.println("asyncShowRoomsList complete");
    }

}
