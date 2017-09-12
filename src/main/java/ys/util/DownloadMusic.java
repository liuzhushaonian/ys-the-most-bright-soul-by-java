package ys.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import ys.pojo.CharacterImpl;
import ys.pojo.ImageImpl;
import ys.pojo.MusicImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DownloadMusic {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();//CPU数量
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;//线程数量
    private static final int MAXNUM_POOL_SIZE = CPU_COUNT * 2 + 1;//最大线程数
    private static final long KEEP_ALIVE = 10L;//时间

    private static final ThreadFactory mThreadFactory = new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "ImageLoader#" + count.getAndIncrement());
        }
    };

    private static final Executor ThreadPool = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXNUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.MINUTES,
            new LinkedBlockingDeque<Runnable>(), mThreadFactory);

    public static void main(String[] args) {
        download();

    }


    public static List<MusicImpl> toMap(JSONArray jsonString) throws JSONException {


        Iterator iterator = jsonString.iterator();
        JSONObject key = null;
        JSONObject value = null;
        List list = new ArrayList();


        while (iterator.hasNext()) {

//            System.out.println(iterator.next());
            key = (JSONObject) iterator.next();
            MusicImpl music = new MusicImpl();
            music.setAlbum("イースVIII -Lacrimosa of DANA- オリジナルサウンドトラック");
            music.setAlbum_pic("https://p4.music.126.net/x27OyAr47WE-TRnYFUTmrg==/3437073357842772.jpg");
            music.setId((Integer) key.get("id"));
            music.setSinger("Falcom Sound Team jdk");
            music.setSong((String) key.get("name"));


//            System.out.println(key.get("id"));


            list.add(music);


        }
        System.out.println("the size is " + list.size());
        return list;

    }

    public static JSONArray test(String json) {
        JSONObject jsonObject = new JSONObject(json);
        Iterator iterator = jsonObject.keys();
        JSONArray jsonArray = new JSONArray();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
//            System.out.println(key.getClass());
            jsonArray.put(jsonObject.get(key));
        }

        return jsonArray;

    }

    /**
     * 通过歌曲id获取歌曲的播放URL
     *
     * @param id
     * @return
     */
    public String getMusicUrl(int id) {


        String urls = "https://api.imjad.cn/cloudmusic/?type=song&id=" + id + "&br=128000";

//        Request.Builder builder = new Request.Builder().url(urls)

        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).retryOnConnectionFailure(true);
        Request request = new Request.Builder().url(urls).build();


//        builder.method("GET", null);
//
//        Request request = builder.build();

        OkHttpClient okHttpClient = builder.build();



        Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();

            JSONArray jsonArray = test(response.body().string());

//            System.out.println(jsonArray.get(1));
            return getMusicUrl((JSONArray) jsonArray.get(1));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }


    public static String getMusicUrl(JSONArray jsonArray) {
        Iterator iterator = jsonArray.iterator();

        JSONObject jsonObject = null;

        while (iterator.hasNext()) {

            jsonObject = (JSONObject) iterator.next();

        }

        return (String) jsonObject.get("url");
    }


    static ExecutorService executor = Executors.newFixedThreadPool(100);


    public void test() {
        try {
            String url = "http://photo.163.com/liuzhushaonian@sina.cn/#m=1&aid=312576012&p=1";


            String html = getHtml(url);

            Document document = Jsoup.parse(html);

            Elements div = document.getElementsByClass("ln");

            Elements links = div.select("a[href]");


            for (Element element : links) {
                getImage("http://photo.163.com/liuzhushaonian@sina.cn/" + element.attr("href"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据url获取网页
     *
     * @param url
     * @return
     */
    private static String getHtml(String url) throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);

        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(20000);
        HtmlPage htmlPage = null;
        htmlPage = webClient.getPage(url);

//设置一个运行JavaScript的时间
        webClient.waitForBackgroundJavaScript(20000);
        String html = htmlPage.asXml();

        return html;
    }


//    ApplicationContext jdbc=new ClassPathXmlApplicationContext("spring_JDBC.xml");


    /**
     * 获取高清大图，开启线程池
     *
     * @param url
     */
    private void getImage(String url) {

        ImageImpl image = new ImageImpl();


//        System.out.println("get the url is "+url);
        try {
            final Document document = Jsoup.parse(getHtml(url));
//            System.out.println("test!!!"+document);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Elements links = document.getElementsByClass("menu");//获取a标签

                    for (Element element : links) {
                        if (element.text().contains("原图")) {
                            System.out.println(element);
                        }
                    }
                }
            };
            executor.execute(runnable);


//            executor.execute(thread);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从Falcom官网下载壁纸
     *
     * @param url
     */
    public static void downloadImagefromFalcom(String url) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).retryOnConnectionFailure(true);
        Request request = new Request.Builder().url(url).build();

//        OkHttpClient okHttpClient=new OkHttpClient();
        OkHttpClient okHttpClient = builder.build();


        Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();

            System.out.println("正在爬取中，线程--"+Thread.currentThread().getName()+"--正在为您工作");


            InputStream inputStream = response.body().byteStream();

//                System.out.println(response.body());

            FileOutputStream fileOutputStream = null;

            String fileNmae = "";


            fileNmae = "/Users/liuzhushaonian/Pictures/falcom";

            File d = new File(fileNmae);

            String name = System.currentTimeMillis() + "--" + Math.random() + ".jpg";


            System.out.println(name);

            File file = new File(d.getAbsolutePath(), name);

            if (null != file) {
                fileOutputStream = new FileOutputStream(file);

                FileChannel fileChannel=fileOutputStream.getChannel();


//                ByteBuffer byteBuffer=ByteBuffer.allocate(4*2048);
                byte[] buffer = new byte[4*2048];

                int len = 0;

                while ((len=inputStream.read(buffer) )!= -1) {
                    fileOutputStream.write(buffer, 0, len);

//                    byteBuffer.flip();
//                    fileChannel.position(0);
//                    fileChannel.write(byteBuffer);
                }


//                fileOutputStream.flush();
                System.out.println("爬取成功！------" + fileNmae + "/" + name);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                try {
//                    System.out.println(call.execute());
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                System.out.println("正在爬取---");
//
//
//                InputStream inputStream=response.body().byteStream();
//
////                System.out.println(response.body());
//
//                FileOutputStream fileOutputStream=null;
//
//                String fileNmae="";
//
//
//
//                fileNmae="/Users/liuzhushaonian/Pictures/falcom";
//
//                File d=new File(fileNmae);
//
//                String name=System.currentTimeMillis()+"--"+Math.random()+".jpg";
//
//
//                System.out.println(name);
//
//                File file=new File(d.getAbsolutePath(),name);
//
//                if (null!=file){
//                    fileOutputStream=new FileOutputStream(file);
//
//                    byte[] buffer=new byte[2048];
//
//                    int len=0;
//
//                    while ((len=inputStream.read(buffer))!=-1){
//                        fileOutputStream.write(buffer,0,len);
//                    }
//
//                    fileOutputStream.flush();
//                    System.out.println("爬取成功！------"+fileNmae+"/"+name);
//                }
//
//
//
//            }
//        });


    }


    public static void download() {
        try {
            String html = getHtml("http://www.falcom.com/download/w_paper/ys_lac.html");
            Document document = Jsoup.parse(html);
            Elements links = document.getElementsByTag("a");
            for (Element element : links) {
                if (element.attr("target").equals("_top")) {
                    Runnable runnable=new Runnable() {
                        @Override
                        public void run() {
                            downloadImagefromFalcom("http://www.falcom.com/download/w_paper/" + element.attr("href"));
                        }
                    };
                    executor.execute(runnable);
//                        System.out.println(element.attr("href"));
                }
            }




        } catch (IOException e) {
            e.printStackTrace();
        }


//        downloadImagefromFalcom("http://www.falcom.com/download/w_paper/pcwp/ys/ys8ps4_02l.jpg");
    }

    public static String getName() {
        String string = "lacrimosa-of-dana" + System.currentTimeMillis() + "/" + Math.random();
        return string;
    }


//    private ApplicationContext jdbc=new ClassPathXmlApplicationContext("spring_JDBC.xml");

    public void getString(File path){
        StringBuffer stringBuffer=new StringBuffer();
        try {
           Files.lines(Paths.get(path.getAbsolutePath()),
                    StandardCharsets.UTF_8).forEach((s)->stringBuffer.append(s+"\n"));

            try {
                CharacterImpl character=new CharacterImpl();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            character.setIntroduction(stringBuffer.toString());
//            character.setId(getId(path.getAbsolutePath()));
//
//            Character mapper=null;//需要实例化
//
//            mapper.insertCharacter(character);



        } catch (IOException e) {
            e.printStackTrace();
        }


    }





    public void writeToDatabase(){
      Stream.of(new File("/Users/liuzhushaonian/Documents/ys/").listFiles())
                .flatMap(file -> file.listFiles()==null?Stream.of(file):Stream.of(file.listFiles()))
                .filter(f ->f.toString().endsWith("md"))
                .forEach(this::getString);
    }



    public int getId(String string){
        Pattern pattern=Pattern.compile("[^0-9]");

        Matcher matcher=pattern.matcher(string);


//        System.out.println(matcher.replaceAll("").trim());

        return Integer.parseInt(matcher.replaceAll("").trim());
    }






}
