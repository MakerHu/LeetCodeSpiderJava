package utils;

import config.Settings;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FileTools {
    private static Settings settings = new Settings();
    static Scanner scanner = new Scanner(System.in);

    public static void writeToFile(String filePath,String content){
        try {

            File file = new File(filePath);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }else{
                System.out.println(filePath);
                System.out.println("该文件已存在！是否覆盖(y/n)?");
                while (true){
                    String isOverwrite = scanner.next();
                    if(isOverwrite.equals("y")){
                        break;
                    }
                    if (isOverwrite.equals("n")){
                        System.exit(0);
                    }
                    System.out.println("无效输入，请重新输入(y/n)：");
                }
            }

//            FileWriter fw = new FileWriter(file.getAbsoluteFile());
//            BufferedWriter bw = new BufferedWriter(fw);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()),"utf-8"));
            bw.write(content);
            bw.close();

            System.out.println("markdown题目生成完成！");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void creatFolder(String pathname){
        File file=new File(pathname);
        if(!file.exists()){//如果文件夹不存在
            file.mkdir();//创建文件夹
        }else{
            System.out.println(pathname);
            System.out.println("该文件夹已存在！是否覆盖(y/n)?");
            while (true){
                String isOverwrite = scanner.next();
                if(isOverwrite.equals("y")){
                    break;
                }
                if (isOverwrite.equals("n")){
                    System.exit(0);
                }
                System.out.println("无效输入，请重新输入(y/n)：");
            }
        }
    }

    /**
     * 复制文件
     * @param oldPath
     * @param newPath
     * @throws IOException
     */
    public static void copyFile(String oldPath, String newPath, String filename) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath + "/" +filename);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);

        byte[] buffer=new byte[2097152];
        int readByte = 0;

        String[] newPathSplit = newPath.split("/");
        String packageStr = "package ";
        if (!settings.getConf("base_package").equals("")){
            packageStr += settings.getConf("base_package") + ".";
        }
        packageStr += newPathSplit[newPathSplit.length-1] + ";";

        out.write(packageStr.getBytes(StandardCharsets.UTF_8),0,packageStr.length());
        while((readByte = in.read(buffer)) != -1){
            out.write(buffer, 0, readByte);
        }

        in.close();
        out.close();
    }
}
