package com.github.catvod.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;

import com.github.catvod.spider.Init;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Util {

    public static final Pattern RULE = Pattern.compile("http((?!http).){12,}?\\.(m3u8|mp4|mkv|flv|mp3|m4a|aac)\\?.*|http((?!http).){12,}\\.(m3u8|mp4|mkv|flv|mp3|m4a|aac)|http((?!http).)*?video/tos*");
    public static final Pattern THUNDER = Pattern.compile("(magnet|thunder|ed2k):.*");
    public static final String CHROME = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36";
    public static final List<String> MEDIA = Arrays.asList("mp4", "mkv", "mov", "wav", "wma", "wmv", "flv", "avi", "iso", "mpg", "ts", "mp3", "aac", "flac", "m4a", "ape", "ogg");
    public static final List<String> SUB = Arrays.asList("srt", "ass", "ssa", "vtt");

    public static boolean isVip(String url) {
        List<String> hosts = Arrays.asList("iqiyi.com", "v.qq.com", "youku.com", "le.com", "tudou.com", "mgtv.com", "sohu.com", "acfun.cn", "bilibili.com", "baofeng.com", "pptv.com");
        for (String host : hosts) if (url.contains(host)) return true;
        return false;
    }

    public static boolean isThunder(String url) {
        return THUNDER.matcher(url).find() || isTorrent(url);
    }

    public static boolean isTorrent(String url) {
        return !url.startsWith("magnet") && url.split(";")[0].endsWith(".torrent");
    }

    public static boolean isVideoFormat(String url) {
        if (url.contains("url=http") || url.contains(".js") || url.contains(".css") || url.contains(".html")) return false;
        return RULE.matcher(url).find();
    }

    public static boolean isSub(String text) {
        return SUB.contains(getExt(text).toLowerCase());
    }

    public static boolean isMedia(String text) {
        return MEDIA.contains(getExt(text).toLowerCase());
    }

    public static String getExt(String name) {
        return name.contains(".") ? name.substring(name.lastIndexOf(".") + 1).toLowerCase() : name.toLowerCase();
    }

    public static String getSize(double size) {
        if (size <= 0) return "";
        String[] units = new String[]{"bytes", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String fixUrl(String base, String src) {
        if (src.startsWith("//")) {
            Uri parse = Uri.parse(base);
            return parse.getScheme() + ":" + src;
        } else if (!src.contains("://")) {
            Uri parse = Uri.parse(base);
            return parse.getScheme() + "://" + parse.getHost() + src;
        } else {
            return src;
        }
    }

    public static String removeExt(String text) {
        return text.contains(".") ? text.substring(0, text.lastIndexOf(".")) : text;
    }

    public static String substring(String text) {
        return substring(text, 1);
    }

    public static String substring(String text, int num) {
        if (text != null && text.length() > num) {
            return text.substring(0, text.length() - num);
        } else {
            return text;
        }
    }

    public static String getVar(String data, String param) {
        for (String var : data.split("var")) if (var.contains(param)) return checkVar(var);
        return "";
    }

    private static String checkVar(String var) {
        if (var.contains("'")) return var.split("'")[1];
        if (var.contains("\"")) return var.split("\"")[1];
        return "";
    }

    public static void copy(String text) {
        ClipboardManager manager = (ClipboardManager) Init.context().getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText("fongmi", text));
        Notify.show("已複製 " + text);
    }
}
