import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Aliyun {
    private static void main(String[] args) throws IOException, InterruptedException {
        // 获取refresh_token并填入
        String refresh_token = "";
        String access_token = updateToken(refresh_token);
        dailyCheck(access_token);

        if (access_token != null) {
            System.out.println("更新成功,开始进行签到");
            String content = dailyCheck(access_token);
            push(content);
        } else {
            System.out.println("更新access_token失败");
        }
    }
//签到结果推送到微信
    private static void push(String contents) throws IOException, InterruptedException {
    //推送加token填入
        String plus_token = "";
        // 推送加
        HttpClient client = HttpClient.newBuilder().build();
        JSONObject json = new JSONObject();
        json.put("token", plus_token);
        json.put("title", "阿里云盘签到");
        json.put("content", contents.replace("\n", "<br>"));
        json.put("template", "json");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://www.pushplus.plus/send"))
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode() == 200 ? "push+推送成功" : "push+推送失败");
    }
//更新access_token
    private static String updateToken(String ali_refresh_token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://auth.aliyundrive.com/v2/account/token"))
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"grant_type\":\"refresh_token\",\"refresh_token\":\"" + ali_refresh_token + "\"}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject result = new JSONObject(response.body());
        return result.getString("access_token");
    }
//阿里云盘签到
    private static String dailyCheck(String access_token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://member.aliyundrive.com/v1/activity/sign_in_list"))
                .header("Content-Type", "application/json")
                .header("Authorization", access_token)
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"checkDate\": \"2020-12-16\"}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject result = new JSONObject(response.body());

        if (result.getBoolean("success")) {
            JSONObject result_1 = result.getJSONObject("result");
            JSONArray signInLogs = result_1.getJSONArray("signInLogs");
            JSONObject day_json = signInLogs.getJSONObject(signInLogs.length() - 2);

            String contents;
            if (day_json.getBoolean("isReward")) {
                contents = "本月累计签到" + result.getJSONObject("result").getInt("signInCount") + "天,今日签到获得"
                        + day_json.getJSONObject("reward").getString("name") + day_json.getJSONObject("reward").getString("description");
            } else {
                contents = "签到成功,今日未获得奖励";
            }
            return contents;
        }
        return null;
    }

}
