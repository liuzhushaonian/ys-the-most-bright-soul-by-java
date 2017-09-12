package ys.pojo;

public class MusicImpl {
    private int id;
    private String url;
    private String album;
    private String song;
    private String singer;
    private String introduction;
    private String album_pic;

    public String getAlbum_pic() {
        return album_pic;
    }

    public void setAlbum_pic(String album_pic) {
        this.album_pic = album_pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
