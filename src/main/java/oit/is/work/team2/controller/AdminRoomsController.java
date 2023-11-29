package oit.is.work.team2.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.work.team2.model.RoomMapper;
import oit.is.work.team2.service.AsyncAdminRoomsService;
import oit.is.work.team2.model.Room;

@Controller
@RequestMapping("/room")
public class AdminRoomsController {

    @Autowired
    RoomMapper roomMapper;

    @Autowired
    AsyncAdminRoomsService adminRoomsService;

    @GetMapping("")
    public String room(ModelMap model) {
        // 単語リストを取得
        final ArrayList<Room> rooms = adminRoomsService.syncShowRoomsList();
        model.addAttribute("rooms", rooms);
        return "adminRooms.html";
    }

    @GetMapping("getList")
    public String getRooms(ModelMap model) {
        // 単語リストを取得
        final ArrayList<Room> rooms = adminRoomsService.syncShowRoomsList();
        model.addAttribute("rooms", rooms);
        return "adminRooms.html";
    }

    @GetMapping("sse")
    public SseEmitter sse() {
        final SseEmitter sseEmitter = new SseEmitter();
        this.adminRoomsService.asyncShowRoomsList(sseEmitter);
        return sseEmitter;
    }

    @GetMapping("deleteRoom")
    @Transactional
    public String deleteRoom(@RequestParam Integer id, ModelMap model) {
        // 選択した単語を削除し，削除対象の単語をmodelに登録
        final Room room1 = this.adminRoomsService.syncDeleteRooms(id);
        model.addAttribute("room1", room1);
        // 残りの単語リストを取得してmodelに登録
        final ArrayList<Room> rooms = adminRoomsService.syncShowRoomsList();
        model.addAttribute("rooms", rooms);
        return "adminRooms.html";
    }

    @GetMapping("editRoom")
    @Transactional
    public String editRoom(@RequestParam Integer id, ModelMap model) {
        // 編集対象の単語を取得
        final Room room2 = this.adminRoomsService.syncEditRooms(id);
        model.addAttribute("room2", room2);
        // 単語リストを取得
        final ArrayList<Room> rooms = adminRoomsService.syncShowRoomsList();
        model.addAttribute("rooms", rooms);
        return "adminRooms.html";
    }

    @PostMapping("updateRoom")
    public String updateRoom(@RequestParam Integer id, @RequestParam String word, ModelMap model) {
        System.out.println("updateRoom");
        System.out.println(id);
        System.out.println(word);
        Room room = new Room(id, word);
        // 編集
        this.adminRoomsService.syncUpdateRooms(room);
        // 単語リストを取得
        final ArrayList<Room> rooms = adminRoomsService.syncShowRoomsList();
        model.addAttribute("rooms", rooms);
        model.addAttribute("beforeRoom", word);
        return "adminRooms.html";
    }

    @PostMapping("addRoom")
    @Transactional
    public String addRoom(ModelMap model, @RequestParam String word) {
        model.addAttribute("addRoom", word);
        // 単語を追加
        this.adminRoomsService.syncAddRooms(word);
        // 単語リストを取得
        final ArrayList<Room> rooms = adminRoomsService.syncShowRoomsList();
        model.addAttribute("rooms", rooms);
        return "adminRooms.html";
    }

}

