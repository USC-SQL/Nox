package sql.usc.MIF;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sql.usc.Color.Color;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dingli on 6/9/15.
 */
public class Image {
    public String path;
    public Set<Color> colorpalette=new HashSet<Color>();
    public Image(String p) throws IOException {
        this.path=p;
        HttpClient client = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://www.cssdrive.com/imagepalette/index.php");
        File file = new File(path);
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("uploadFile", cbFile);
        httppost.setEntity(mpEntity);
        client.execute(httppost);
        HttpGet httpget = new HttpGet("http://www.cssdrive.com/imagepalette/export.php?e=css");
        HttpResponse response2 = client.execute(httpget);
        HttpEntity resEntity2 = response2.getEntity();
        String r="";
        if (resEntity2 != null) {
            r=EntityUtils.toString(resEntity2);
        }
        String [] colorstrings=r.split("<br/>");
       for(String c:colorstrings)
       {
           Pattern pattern = Pattern.compile(".*(#[A-Za-z0-9]+).*");
           Matcher m=pattern.matcher(c);
           if(m.matches())
               colorpalette.add(new Color(m.group(1)));
       }

    }
}
