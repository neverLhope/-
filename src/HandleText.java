import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by never on 2016/11/3.
 */
public class HandleText {
    static String basePath = "演示语料";

    public static List<String> wordList = new ArrayList<String>();
    public static Map<String, Integer> wordMap = new HashMap<>();

    public static int numWords = 0;
    public static int numTxt = 0;

    public static void findFile(File dir) throws IOException {
        File[] dirFiles = dir.listFiles();
        for (File temp : dirFiles) {
            if (!temp.isFile()) {
                findFile(temp);
            }
            if (temp.isFile() && temp.getAbsolutePath().endsWith(".txt")) {
                numTxt++;
                String context = readFileContent(temp);
            }
        }
    }

    private static boolean isIn(String s, List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (s.equals(list.get(i)))
                return true;
        }
        return false;
    }

    private static boolean isIn(String s, Map<String, Integer> map) {
        if (map.get(s) != null) {
            return true;
        }
        return false;
    }

    //最大正向匹配
    public static String rightMax(StringBuilder context) {

        String s = "";
        int len = 0;
        int l = context.length();
        for (int i = 0; i < l - 1; i++) {
            len = i + 12;
            while (true) {
                if (len > l - 1) {
                    len = l - 1;
                }
                s = context.substring(i, len);
                if (isIn(s, wordMap)) {
                    context.insert(len, "/");
                    i = len;
                    break;
                } else if (len - i != 1) {
                    len--;
                } else {
                    context.insert(len, "/");
                    i = len;
                    break;
                }
            }
            l = context.length();
        }
        return context.toString();
    }


//    public static void handleTxtResult(String context) {
//        String s = rightMax(context);
//        System.out.println(s);
////        int len = context.length() - 1;
////        int tLen = context.length();
////        String str = null;
////        StringBuilder sb = new StringBuilder(context);;
////        while (tLen > 0) {
////            tLen = len - 7;
////            if (tLen < 0) {
////                tLen = 0;
////            }
////            int t = len - tLen;
////            for(int i = 0; i < t; i++){
////                str = sb.substring(tLen, len);
////                //sb = new StringBuilder(str);
////                if(len == tLen){
////                    sb.insert(tLen, "aa");
////                    break;
////                }
////                if(wordList.contains(str)){
////                    sb.insert(tLen, "aa");
////                    break;
////                }
////                tLen++;
////            }
////            len = tLen - 3;
////        }
////        System.out.println(sb.toString());
//    }

    public static String readFileContent(File file) throws IOException {
        InputStreamReader fr = new InputStreamReader(new FileInputStream(file), "gbk");
        BufferedReader br = new BufferedReader(fr);
        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append(br.readLine());
        }
        String s = rightMax(sb);
        writeFileContent(file.toString().substring(4, file.toString().length()), s);
        System.out.println(s);
        return sb.toString();
    }

    public static List<String> readWordsText() throws IOException {

        Scanner sc = new Scanner(new FileInputStream("搜狗词库.txt"), "gbk");
        while (sc.hasNext()) {
            String s = sc.next();
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(s);
            if (m.find()) {
                wordMap.put(s, 1);
                //wordList.add(s);
                numWords++;
            }
        }
        return wordList;
    }

    /**
     * * @param file 要写入的文件对象
     * * @param content 要写入的文件内容
     */
    public static void writeFileContent(String fileName, String content) throws IOException {
        File f = new File("resultTxt");
        if (!f.exists()) {
            f.mkdir();
        }
        FileWriter fw = new FileWriter(f + "\\" + fileName, true);
        fw.write(content);
        fw.flush();
        fw.close();
    }

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        try {
            wordList = readWordsText();
            long endTime1 = System.currentTimeMillis();
            findFile(new File(basePath));
            long endTime = System.currentTimeMillis();

            System.out.println("词典处理时间：" + (endTime1 - startTime) + "ms");
            System.out.println("文本数量：" + numTxt);
            System.out.println("单词数量：" + numWords);
            System.out.println("分词搜用时间：" + (endTime - endTime1) + "ms");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
