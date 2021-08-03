import config.Settings;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import utils.FileTools;


import java.io.IOException;
import java.util.Scanner;

public class SpiderApplication {
    public static void main(String... args) throws IOException, JSONException {
        Settings settings = new Settings();

        JSONObject jsonObj;
        JSONObject dataObj;

        while(true){
            System.out.println("请输入LeetCode中题目的英文名（见浏览器路径，不带斜杠）：");
            Scanner scanner = new Scanner(System.in);

            String questionName = scanner.next();

//        String questionUrl = "https://leetcode.com/problems/two-sum/description/";
            String questionUrl = "https://leetcode-cn.com/problems/" + questionName + "/description/";
            String graphqlUrl = "https://leetcode-cn.com/graphql/";
            Connection.Response response = Jsoup.connect(questionUrl)
                    .method(Connection.Method.GET)
                    .execute();


//        String csrftoken = response.cookie("csrftoken");
            String csrftoken = "g2B7f2I2PVERrR160QCgLvdTeX2CY2KWXoiXtsNlNMxrNzSsNA5esgVVrhvx4PZs";
            String __cfduid = response.cookie("__cfduid");

            OkHttpClient client = new OkHttpClient.Builder()
                    .followRedirects(false)
                    .followSslRedirects(false)
                    .build();

            String postBody = "query{\n" +
                    "  question(titleSlug:\""+questionName+"\") {\n" +
                    "    questionFrontendId\n" +
                    "    translatedTitle\n" +
                    "    titleSlug\n" +
                    "    translatedContent\n" +
                    "  }\n" +
                    "}\n";

            Request request = new Request.Builder()
                    .addHeader("Content-Type", "application/graphql")
                    .addHeader("Referer", questionUrl)
                    .addHeader("Cookie", "__cfduid=" + __cfduid + ";" + "csrftoken=" + csrftoken)
                    .addHeader("x-csrftoken", csrftoken)
                    .url(graphqlUrl)
                    .post(RequestBody.create(MediaType.parse("application/graphql; charset=utf-8"), postBody))
                    .build();

            Response response1 = client.newCall(request).execute();
            String questionStr = response1.body().string();


            jsonObj = new JSONObject(questionStr);
            dataObj = (JSONObject) jsonObj.get("data");

            if (dataObj.getString("question").equals("null")){
                System.out.println("题目不存在！(0.退出 1：输入新题目)");
                String choose;
                while (true){
                    choose = scanner.next();
                    if(choose.equals("0")) System.exit(0);
                    else if (choose.equals("1")) break;
                    else System.out.println("无效输入，请重新选择(0.退出 1：输入新题目)");
                }
                if(choose.equals("1")) continue;
            }else{
                break;
            }
        }

        JSONObject questionObj = (JSONObject) dataObj.get("question");

        String questionFrontendId = questionObj.getString("questionFrontendId");
        String translatedTitle = questionObj.getString("translatedTitle");
        String titleSlug = questionObj.getString("titleSlug");
        String questionContent = questionObj.getString("translatedContent");

        questionContent = "## " + questionFrontendId + "." + translatedTitle + "\n\n" + questionContent;

        System.out.println("************已获取到题目数据************");
        System.out.println("-------------------------------------");
        System.out.println("题号：" + questionFrontendId);
        System.out.println("题目：" + translatedTitle);
        System.out.println("-------------------------------------");
        System.out.println();
        System.out.println("************开始生成本地项目************");
        System.out.println("-------------------------------------");


        // 文件创建的基础路径
        String basePath = settings.getConf("question_location");
        // 文件夹的名称
        String folderName = settings.getConf("folder.prefix") + questionFrontendId;
        if (settings.getConf("folder.include_question_name").equals("true")){
            folderName = folderName + "_" + titleSlug.replace("-","_");
        }

        // 创建文件夹
        String folderPath = basePath + "/" +folderName;
        FileTools.creatFolder(folderPath);
        System.out.println("项目位置：" + folderPath);

        // 创建题目markdown文件
        String markdownQuestionName = settings.getConf("question_markdown_filename.prefix");
        if(settings.getConf("question_markdown_filename.include_question_id").equals("true")){
            markdownQuestionName = markdownQuestionName + "_" +questionFrontendId;
        }
        if(settings.getConf("question_markdown_filename.include_question_name").equals("true")){
            if(settings.getConf("question_markdown_filename.zh_cn").equals("true")){
                markdownQuestionName = markdownQuestionName + "_" +translatedTitle;
            }else {
                markdownQuestionName = markdownQuestionName + "_" +titleSlug.replace("-","_");
            }

        }
        String questionPath = folderPath + "/" + markdownQuestionName;

        FileTools.writeToFile( questionPath+ ".md",questionContent);
        System.out.println("题目已克隆到本地");

        // 创建Solution.java文件
        FileTools.copyFile("src/main/resources/template/Solution.java",folderPath,"Solution.java");
        System.out.println("已生成Solution.java");

        // 创建Main.java文件
        FileTools.copyFile("src/main/resources/template/Main.java",folderPath,"Main.java");
        System.out.println("已生成Main.java");
        System.out.println("项目初始化完成，可开始解题！");
        System.out.println("-------------------------------------");

    }

}
