//package com.sz.www.test.controller;
//
//import com.sz.admin.hotels.service.HotelsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.sql.*;
//import java.util.HashMap;
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Map;
//
//public class UpdateImageFields {
//    private static final RedisTemplate redisTemplate = new RedisTemplate<>();
////    // 获取数据库连接（请根据实际情况修改 URL、用户名和密码）
////    private static Connection getConnection() throws SQLException {
////        String url = "jdbc:mysql://127.0.0.1:3306/admin_backend?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false&allowPublicKeyRetrieval=true";
////        String user = "root";
////        String password = "123456";
////        return DriverManager.getConnection(url, user, password);
////    }
////
////    // 更新 hotels 表的 img 字段
////    public static void updateHotelsImg(List<String> imgList) {
////        String selectSql = "SELECT hotel_id FROM hotels";
////        String updateSql = "UPDATE hotels SET img = ? WHERE hotel_id = ?";
////
////        try (Connection conn = getConnection();
////             Statement stmt = conn.createStatement();
////             ResultSet rs = stmt.executeQuery(selectSql);
////             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
////
////            // 收集所有酒店记录的 id
////            List<String> hotelIds = new ArrayList<>();
////            while (rs.next()) {
////                hotelIds.add(rs.getString("hotel_id"));
////            }
////
////            // 如果记录数多于图片数，可以循环使用图片列表
////            int imgIndex = 0;
////            for (String id : hotelIds) {
////                String imgUrl = imgList.get(imgIndex);
////                pstmt.setString(1, imgUrl);
////                pstmt.setString(2, id);
////                pstmt.executeUpdate();
////
////                imgIndex++;
////                if (imgIndex >= imgList.size()) {
////                    imgIndex = 0; // 循环使用图片列表
////                }
////            }
////
////            System.out.println("hotels 表的 img 字段更新完成！");
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////    }
////
////    // 更新 room_types 表的 photos_urls 字段
////    public static void updateRoomTypesPhotos(List<String> photoUrls) {
////        // 注意：如果你的表名为 "room-types"，可能需要加反引号 `room-types`
////        String selectSql = "SELECT room_type_id FROM room_types";
////        String updateSql = "UPDATE room_types SET photo_urls = ? WHERE room_type_id = ?";
////
////        try (Connection conn = getConnection();
////             Statement stmt = conn.createStatement();
////             ResultSet rs = stmt.executeQuery(selectSql);
////             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
////
////            // 收集所有 room_types 记录的 id
////            List<String> roomTypeIds = new ArrayList<>();
////            while (rs.next()) {
////                roomTypeIds.add(rs.getString("room_type_id"));
////            }
////
////            // 如果记录数多于图片数，同样循环遍历图片列表
////            int photoIndex = 0;
////            for (String id : roomTypeIds) {
////                String photoUrl = photoUrls.get(photoIndex);
////                pstmt.setString(1, photoUrl);
////                pstmt.setString(2, id);
////                pstmt.executeUpdate();
////
////                photoIndex++;
////                if (photoIndex >= photoUrls.size()) {
////                    photoIndex = 0; // 循环使用图片列表
////                }
////            }
////
////            System.out.println("room_types 表的 photos_urls 字段更新完成！");
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////    }
//    private final HotelsService hotelsService;
//    // 主方法示例
//    public static void main(String[] args) {
//
//        // 示例图片列表（实际数据请根据需求加载）
//        List<String> hotelsImgs = List.of(
//                "https://img1.tucang.cc/api/image/show/cc896e769280726a2b4f3be524b8838b",
//                "https://img1.tucang.cc/api/image/show/ac33b5f0b3732f3e4555da8ad127c131",
//                "https://img1.tucang.cc/api/image/show/3f1667c1a38692b9106bc15eacc8bff4",
//                "https://img1.tucang.cc/api/image/show/c886b6a404a962697594ad26422993b6",
//                "https://img1.tucang.cc/api/image/show/1d40d07ccba1011c835df6303c3897dd",
//                "https://img1.tucang.cc/api/image/show/4af625587067dadd33a71bbb0c8f9630",
//                "https://img1.tucang.cc/api/image/show/f7c6454037fb07179e9c03815238ba14",
//                "https://img1.tucang.cc/api/image/show/dee9ff80a7177e1c2b7e1abc42ecdfe1",
//                "https://img1.tucang.cc/api/image/show/7917f43149fe8a1341b837c5fc7c3947",
//                "https://img1.tucang.cc/api/image/show/eb630c185eef673cc7e20ef26b4557b4",
//                "https://img1.tucang.cc/api/image/show/ac70f39cc0bc4778130b262022f84005",
//                "https://img1.tucang.cc/api/image/show/a0a0dcffc5f65ccaa624ded1914434a9",
//                "https://img1.tucang.cc/api/image/show/ff8540eeae82e30c4bd4304cec56f0b3",
//                "https://img1.tucang.cc/api/image/show/5209db283e8e65e30d2a0c5c59599cb7",
//                "https://img1.tucang.cc/api/image/show/17785f7579c48f9b838442a3de869abc",
//                "https://img1.tucang.cc/api/image/show/bc45bc50129eb5c743ca9b91f8607643",
//                "https://img1.tucang.cc/api/image/show/df2245e094996264b376ccd1704af909",
//                "https://img1.tucang.cc/api/image/show/81b1e2ee352e787b5f03707d9dea470e",
//                "https://img1.tucang.cc/api/image/show/fe90b220aff9d6c31cfba88c307930cd",
//                "https://img1.tucang.cc/api/image/show/cd437478ad1c683dbe2aca364fbaafca",
//                "https://img1.tucang.cc/api/image/show/390c87aec57db8d4a216b77c1a52fceb",
//                "https://img1.tucang.cc/api/image/show/e75b2d8cfff60642bc9b43b1d145db24",
//                "https://img1.tucang.cc/api/image/show/42842c66a2dd2d09df5f3da69377bc39",
//                "https://img1.tucang.cc/api/image/show/f7371c0d5b12eb53f70c1422606f1b09",
//                "https://img1.tucang.cc/api/image/show/f895ee0ff108586cefcb9ed7c0f745ff",
//                "https://img1.tucang.cc/api/image/show/2ce7271c04ba6b514fb717b180d4ddad",
//                "https://img1.tucang.cc/api/image/show/51a994629569584ed494dc36b7984af8",
//                "https://img1.tucang.cc/api/image/show/ee728996208fc4585724a735731fb1e3",
//                "https://img1.tucang.cc/api/image/show/4eea020564296af8ccf7b37bccbd0c9f",
//                "https://img1.tucang.cc/api/image/show/3584ed69ca22dfb8fb9aecde25af1802",
//                "https://img1.tucang.cc/api/image/show/3f972b7e580833c8c07623737d0620ad",
//                "https://img1.tucang.cc/api/image/show/93f2f5368915d8f9c595dfcf94214785",
//                "https://img1.tucang.cc/api/image/show/25c396c967f47a546d3c56b1d89286c0",
//                "https://img1.tucang.cc/api/image/show/4f80856a145f1e103b2e0050a0a49583",
//                "https://img1.tucang.cc/api/image/show/9f35b841a3ae8b5d85e104cc5e723152",
//                "https://img1.tucang.cc/api/image/show/dfee6c662b82163ec83ca44f3b0a6632",
//                "https://img1.tucang.cc/api/image/show/5ea57dcaab6b997ffd7aefb7259df614",
//                "https://img1.tucang.cc/api/image/show/1369a1842b1c600be40f1bc2417d6ca4",
//                "https://img1.tucang.cc/api/image/show/0345e09c2a9b735759bb5c72802072c6",
//                "https://img1.tucang.cc/api/image/show/056e48d495e58edcf3d4515a90ea075b",
//                "https://img1.tucang.cc/api/image/show/9ca25bf2b7bd96b823f45c0f5608b0ab",
//                "https://img1.tucang.cc/api/image/show/68601c7124af066b2f407ddc17f3c32b",
//                "https://img1.tucang.cc/api/image/show/59ba8556ceba33da1f69443137a16763",
//                "https://img1.tucang.cc/api/image/show/d1392bb25bcb689779cb1c78e426be46",
//                "https://img1.tucang.cc/api/image/show/fe12fa1c41ca854e6c236d84c3f1df3e",
//                "https://img1.tucang.cc/api/image/show/b0e9b22a0de27e2c18d81cd54dcf9585",
//                "https://img1.tucang.cc/api/image/show/43ac13219882f63b5fbb3302f043838a",
//                "https://img1.tucang.cc/api/image/show/0f002e1c0e79ee8e915827d910a3eaf4",
//                "https://img1.tucang.cc/api/image/show/dbce1595a67b4a14fd33c920e5616eb5",
//                "https://img1.tucang.cc/api/image/show/c5d346e0612804f3178b1e9f8f970803",
//                "https://img1.tucang.cc/api/image/show/84e8f97670cd3c1ec5b73f7247f14dd6",
//                "https://img1.tucang.cc/api/image/show/40ab4cbc43badeddad8c93450aef4d10"
//
//        );
//
//        List<String> roomTypePhotos = List.of(
//                "https://img1.tucang.cc/api/image/show/6d87277e71c1ccbb5c3abbee7cb9e521",
//                "https://img1.tucang.cc/api/image/show/a39e5e8bea9a4101084fef64dbbd55de",
//                "https://img1.tucang.cc/api/image/show/bef95a29d2d0918ce59c01655162587d",
//                "https://img1.tucang.cc/api/image/show/4f8c7fa92dba8f371517137f054f7268",
//                "https://img1.tucang.cc/api/image/show/5d3e18c6be23931de8d3b29569fcb567",
//                "https://img1.tucang.cc/api/image/show/fbf6405eb9c375a17bd46b167ae71e57",
//                "https://img1.tucang.cc/api/image/show/2a15c6681fb911588222eb7617f0835b",
//                "https://img1.tucang.cc/api/image/show/86a6c9e076f4f18b4942d072ece750d3",
//                "https://img1.tucang.cc/api/image/show/15ecd5a4cb49edd1739fb0cd60f891dd",
//                "https://img1.tucang.cc/api/image/show/3f43c884ec2dff9b4e316636993fb6cc",
//                "https://img1.tucang.cc/api/image/show/1128c7f6616b8cfcfa62f09ccdf5fe45",
//                "https://img1.tucang.cc/api/image/show/f999d56654b1b24ef4fd0ee5fca73b3f",
//                "https://img1.tucang.cc/api/image/show/3731edff286f1070b0a2419d79751f65",
//                "https://img1.tucang.cc/api/image/show/98ad88f1a635fc26a00d6eed6d1e8d3f",
//                "https://img1.tucang.cc/api/image/show/a0e57b428f6eaf16ed1f504f6a660bb1",
//                "https://img1.tucang.cc/api/image/show/ee69dbae09291ce49a8b974aa0c26d64",
//                "https://img1.tucang.cc/api/image/show/fe94928dfb6984559d7b9ca155e13434",
//                "https://img1.tucang.cc/api/image/show/bc61ab17940f4c70d8eef413138ebc35",
//                "https://img1.tucang.cc/api/image/show/c81c4371243af5289c952e232e2d969c",
//                "https://img1.tucang.cc/api/image/show/33a1f400c839131d03eed31ed5f8a367",
//                "https://img1.tucang.cc/api/image/show/62b6045467d5d0bbd83e1a69a266575d",
//                "https://img1.tucang.cc/api/image/show/92a51f7fa466e4db009dbb18acd9ebe4",
//                "https://img1.tucang.cc/api/image/show/ec816816cb9dbeb82266fc1f0e07cd37",
//                "https://img1.tucang.cc/api/image/show/451df148544fa8c180cee925f2b161be",
//                "https://img1.tucang.cc/api/image/show/5007068a0830ddc44110b44517d401e6",
//                "https://img1.tucang.cc/api/image/show/ffd41c45d24cab6ddbba44b1c6b7cac6",
//                "https://img1.tucang.cc/api/image/show/7e5f0a22e54979063e98bd010209f65f",
//                "https://img1.tucang.cc/api/image/show/e1dd1cb1329b3e46f20497b2567e09ce",
//                "https://img1.tucang.cc/api/image/show/84aa7961019dd5088a6caf9320ca90d5",
//                "https://img1.tucang.cc/api/image/show/51148907155259773bb905a15f1641ae"
//        );
//
////        updateHotelsImg(hotelsImgs);
////        updateRoomTypesPhotos(roomTypePhotos);
//    }
//}
