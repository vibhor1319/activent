import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.ByteArrayInputStream;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.time.SUTime.Temporal;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;

import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Generics;
import edu.stanford.nlp.util.TwoDimensionalMap.Entry;


public class SUTimeSimpleParser {

  private SUTimeSimpleParser() {} // static methods

  public static class SUTimeParsingError extends Exception {
    private static final long serialVersionUID = 1L;
    public String timeExpression;

    public SUTimeParsingError(String timeExpression) {
      this.timeExpression = timeExpression;
    }

    public String getLocalizedMessage() {
      return "Error while parsing '" + timeExpression + "'";
    }

  }

  private static AnnotationPipeline pipeline;
  private static Map<String, Temporal> cache;
  public static int calls = 0;
  public static int misses = 0;

  static {
    pipeline = makeNumericPipeline();
    cache = Generics.newHashMap();
  }

  private static AnnotationPipeline makeNumericPipeline() {
    AnnotationPipeline pipeline = new AnnotationPipeline();
    pipeline.addAnnotator(new TokenizerAnnotator(false, "en"));
    pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
    pipeline.addAnnotator(new POSTaggerAnnotator(false));
    pipeline.addAnnotator(new TimeAnnotator(true));

    return pipeline;
  }


  public static void parse(String str) throws SUTimeParsingError {
    try {
      Annotation doc = new Annotation(str);
      pipeline.annotate(doc);

      assert doc.get(CoreAnnotations.SentencesAnnotation.class) != null;
      assert doc.get(CoreAnnotations.SentencesAnnotation.class).size() > 0;
      List<CoreMap> timexAnnotations = doc.get(TimeAnnotations.TimexAnnotations.class);
      int size=timexAnnotations.size();
      ArrayList<String> al=new ArrayList<String>();
      for(int i=0;i<size;i++)
      {
    	  CoreMap timex = timexAnnotations.get(i);
    	  Temporal ex=timex.get(TimeExpression.Annotation.class).getTemporal();
    	 System.out.println(ex);
      }

    } catch (Exception e) {
      SUTimeSimpleParser.SUTimeParsingError parsingError = new SUTimeSimpleParser.SUTimeParsingError(str);
      parsingError.initCause(e);
      throw parsingError;
    }
  }



  public static void main(String[] args) throws SUTimeParsingError, MessagingException {
	HashMap<String,String>  hm=new HashMap<String,String>();
	hm.put("kk","ghhj");
	for(java.util.Map.Entry<String, String> e: hm.entrySet()){
		//System.out.println("Key " + e.getKey());
		//System.out.println("Value " + e.getValue());

	}

    String s="NextPart_000_005D_01CC73D5.3BA43FB0\nContent-Type: text/plain;\n\tcharset=\"iso-8859-1\"\nContent-Transfer-Encoding: quoted-printable\n\nStuff:\n\n            Please read this stuff at the beginning of each week.  =\nFeel free to discuss it throughout the week.\n\n\n--=20\n\nMrs. Suzy M. Smith\n555-555-5555\nsuzy@suzy.com\n------=_NextPart_000_005D_01CC73D5.3BA43FB0\nContent-Type: text/html;\n\tcharset=\"iso-8859-1\"\nContent-Transfer-Encoding: quoted-printable\n\n<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n<HTML><HEAD>\n<META content=3D\"text/html; charset=3Diso-8859-1\" =\nhttp-equiv=3DContent-Type>\n<META name=3DGENERATOR content=3D\"MSHTML 9.00.8112.16434\">\n<STYLE></STYLE>\n</HEAD>\n<BODY bgColor=3D#ffffff>\n<DIV>\n<P style=3D\"MARGIN: 0in 0in 0pt\" class=3DMsoNormal><SPAN=20\nstyle=3D\"mso-bidi-font-size: 12.0pt; mso-fareast-font-family: 'Times New =\nRoman'; mso-bidi-font-family: 'Times New =\nRoman'\">Stuff:<?xml:namespace=20\nprefix =3D o ns =3D \"urn:schemas-microsoft-com:office:office\"=20\n/><o:p></o:p></SPAN></P>\n<P style=3D\"MARGIN: 0in 0in 0pt\" class=3DMsoNormal><SPAN=20\nstyle=3D\"mso-bidi-font-size: 12.0pt; mso-fareast-font-family: 'Times New =\nRoman'; mso-bidi-font-family: 'Times New Roman'\"><SPAN=20\nstyle=3D\"mso-tab-count: =\n1\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;=20\n</SPAN></SPAN>Please read this stuff at the beginning of each =\nweek.&nbsp; Feel=20\nfree to discuss it throughout the week.<SPAN=20\nstyle=3D\"mso-bidi-font-size: 12.0pt; mso-fareast-font-family: 'Times New =\nRoman'; mso-bidi-font-family: 'Times New =\nRoman'\"><o:p></o:p></SPAN></P></DIV>\n<DIV><BR>-- <BR><BR>Mrs. Suzy M. Smith<BR>555-555-5555<BR><A=20\nhref=3D\"mailto:suzu@suzy.com\">suzy@suzy.com</A></DIV></BODY=\n></HTML>\n\n------=_NextPart_000_005D_01CC73D5.3BA43FB0--\n\n";
    InputStream stream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    Session ss = Session.getDefaultInstance(new Properties());
    MimeMessage mi=new MimeMessage(ss,stream);
    mi.getAllHeaderLines();
    for (Enumeration<Header> e = mi.getAllHeaders(); e.hasMoreElements();) {
        Header h = e.nextElement();
        String name=h.getName();
        String value=h.getValue();
        System.out.println(value);
        h.getValue();
    }
    //System.out.println("String: " + s);
      //parse(s);
      System.out.println();

  }

}