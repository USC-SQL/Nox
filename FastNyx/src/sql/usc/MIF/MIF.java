package sql.usc.MIF;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import sql.usc.Color.Color;
import wam.configuration.WAMConfiguration;
import wam.configuration.exceptions.WAMConfigurationException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dingli on 6/9/15.
 */
public class MIF {
    public boolean usetemplate=false;
    public String classconfig="";
    public String classpath="";
    public Color bgcolor=null;
    public String hookerpath="";
    public String modifyscriptpath="";
    public String libpath="";
    public String templatepath="";
    public Set<Image> images=new HashSet<Image>();
    public String csspath;
    private void setcsspath(Document dom)
    {

        NodeList nList=dom.getElementsByTagName("csspath");
        if(nList.getLength()==0)
        {
            this.csspath="None";
            return;
        }
        if(nList.getLength()>1)
        {
            throw new Error("Have multiple csspath in xml");

        }
        Node n=nList.item(0);
        NamedNodeMap attributes = n.getAttributes();

        for (int a = 0; a < attributes.getLength(); a++)
        {
            Node theAttribute = attributes.item(a);
            if(theAttribute.getNodeName().equals("path"))
            {
                String value=theAttribute.getNodeValue();

                this.csspath=value;

            }
        }
        System.out.println(this.csspath);
    }
    private void settemplatepath(Document dom)
    {
        if(!this.usetemplate)
            return;
        NodeList nList=dom.getElementsByTagName("templatepath");
        if(nList.getLength()==0)
        {
            throw new Error("Need one templatepath");
        }
        if(nList.getLength()>1)
        {
            throw new Error("Have multiple templatepath in xml");

        }
        Node n=nList.item(0);
        NamedNodeMap attributes = n.getAttributes();

        for (int a = 0; a < attributes.getLength(); a++)
        {
            Node theAttribute = attributes.item(a);
            if(theAttribute.getNodeName().equals("path"))
            {
                String value=theAttribute.getNodeValue();

                this.templatepath=value;

            }
        }
        System.out.println(this.templatepath);
    }
    private void setlibpath(Document dom)
    {
        if(this.usetemplate)
            return;
        NodeList nList=dom.getElementsByTagName("libpath");
        if(nList.getLength()==0)
        {
            throw new Error("Need one libpath");
        }
        if(nList.getLength()>1)
        {
            throw new Error("Have multiple libpath in xml");

        }
        Node n=nList.item(0);
        NamedNodeMap attributes = n.getAttributes();

        for (int a = 0; a < attributes.getLength(); a++)
        {
            Node theAttribute = attributes.item(a);
            if(theAttribute.getNodeName().equals("path"))
            {
                String value=theAttribute.getNodeValue();

                this.libpath=value;

            }
        }
        System.out.println("lib "+this.libpath);
    }
    private void loadimages(Document dom) throws IOException {
        NodeList nList=dom.getElementsByTagName("imagepath");
       for(int i=0;i<nList.getLength();i++)
       {
           Node n=nList.item(i);
           NamedNodeMap attributes = n.getAttributes();

           for (int a = 0; a < attributes.getLength(); a++)
           {
               Node theAttribute = attributes.item(a);
               if(theAttribute.getNodeName().equals("path"))
               {
                   String value=theAttribute.getNodeValue();
                   Image img=new Image(value);
                   images.add(img);

               }
           }
       }


    }
    private void setmodifyscriptpath(Document dom)
    {
        NodeList nList=dom.getElementsByTagName("modifyscriptpath");
        if(nList.getLength()==0)
        {
            throw new Error("Need one modifyscriptpath");
        }
        if(nList.getLength()>1)
        {
            throw new Error("Have multiple modifyscriptpath in xml");

        }
        Node n=nList.item(0);
        NamedNodeMap attributes = n.getAttributes();

        for (int a = 0; a < attributes.getLength(); a++)
        {
            Node theAttribute = attributes.item(a);
            if(theAttribute.getNodeName().equals("path"))
            {
                String value=theAttribute.getNodeValue();

                this.modifyscriptpath=value;

            }
        }
        System.out.println(this.modifyscriptpath);
    }

    private void sethookerpath(Document dom)
    {
        if(this.usetemplate)
            return;
        NodeList nList=dom.getElementsByTagName("hookerpath");
        if(nList.getLength()==0)
        {
            throw new Error("Need one hookerpath");
        }
        if(nList.getLength()>1)
        {
            throw new Error("Have multiple hookerpath in xml");

        }
        Node n=nList.item(0);
        NamedNodeMap attributes = n.getAttributes();

        for (int a = 0; a < attributes.getLength(); a++)
        {
            Node theAttribute = attributes.item(a);
            if(theAttribute.getNodeName().equals("path"))
            {
                String value=theAttribute.getNodeValue();
                this.hookerpath=value;

            }
        }
        System.out.println(this.hookerpath);

    }
    private void setbgcolor(Document dom)
    {
        NodeList nList=dom.getElementsByTagName("bgcolor");
        if(nList.getLength()==0)
        {
            throw new Error("Need one bgcolor");
        }
        if(nList.getLength()>1)
        {
            throw new Error("Have multiple bgcolor in xml");

        }
        Node n=nList.item(0);
        NamedNodeMap attributes = n.getAttributes();

        for (int a = 0; a < attributes.getLength(); a++)
        {
            Node theAttribute = attributes.item(a);
            if(theAttribute.getNodeName().equals("color"))
            {
                String value=theAttribute.getNodeValue();
                this.bgcolor=new Color(value);

            }
        }
        System.out.println(this.bgcolor);

    }
    private void setusetemplate(Document dom)
    {
        NodeList nList=dom.getElementsByTagName("usetemplate");
        if(nList.getLength()==0)
        {
            throw new Error("Need one usetemplate");
        }
        if(nList.getLength()>1)
        {
            throw new Error("Have multiple usetemplate in xml");

        }
        Node n=nList.item(0);
        NamedNodeMap attributes = n.getAttributes();

        for (int a = 0; a < attributes.getLength(); a++)
        {
            Node theAttribute = attributes.item(a);
            if(theAttribute.getNodeName().equals("value"))
            {
                String value=theAttribute.getNodeValue();
                if(value.equals("false"))
                {
                    this.usetemplate=false;
                }
                else{
                    this.usetemplate=true;

                }

            }
        }

    }
    private void setClassConfigPath(Document dom) throws WAMConfigurationException {
        if(this.usetemplate)
            return;
        NodeList nList=dom.getElementsByTagName("classconfig");
        if(nList==null || nList.getLength()==0)
        {
            throw new Error("Need one classconfig");
        }
        if(nList.getLength()>1)
        {
            throw new Error("Have multiple classconfig in xml");

        }
        Node n=nList.item(0);
        NamedNodeMap attributes = n.getAttributes();

        for (int a = 0; a < attributes.getLength(); a++)
        {
            Node theAttribute = attributes.item(a);
            if(theAttribute.getNodeName().equals("path"))
            {
                String value=theAttribute.getNodeValue();
                this.classconfig=value;
                WAMConfiguration conf=WAMConfiguration.load( this.classconfig);
                this.classpath=conf.getClassPath();


            }
        }
        System.out.println( this.classconfig);

    }
    public MIF(String path)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            Document dom = db.parse(path);
            setusetemplate(dom);
            setClassConfigPath(dom);
            setbgcolor(dom);
            sethookerpath(dom);
            setmodifyscriptpath(dom);
            loadimages(dom);
            setlibpath(dom);
            settemplatepath(dom);
            setcsspath(dom);
        }catch(ParserConfigurationException pce) {
            pce.printStackTrace();
        }catch(SAXException se) {
            se.printStackTrace();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        } catch (WAMConfigurationException e) {
            e.printStackTrace();
        }
    }

}
