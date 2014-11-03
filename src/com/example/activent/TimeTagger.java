package com.example.activent;

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
import android.annotation.SuppressLint;
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


@SuppressLint("NewApi") public class TimeTagger {

  public TimeTagger() {} // static methods

 /* public static class SUTimeParsingError extends Exception {
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
      TimeTagger.SUTimeParsingError parsingError = new TimeTagger.SUTimeParsingError(str);
      parsingError.initCause(e);
      throw parsingError;
    }
  }

*/

  @SuppressLint("NewApi") public static void run_me(ArrayList<String> args) throws MessagingException {
	//HashMap<String,String>  hm=new HashMap<String,String>();
	//hm.put("kk","ghhj");
	/*for(java.util.Map.Entry<String, String> e: hm.entrySet()){
		//System.out.println("Key " + e.getKey());
		//System.out.println("Value " + e.getValue());
		
	}
	*/
	  for(String s:args)
	  {
  //  String s="";
    InputStream stream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    Session ss = Session.getDefaultInstance(new Properties());
    MimeMessage mi=new MimeMessage(ss,stream); 
    mi.getAllHeaderLines();
    for (Enumeration<Header> e = mi.getAllHeaders(); e.hasMoreElements();) {
        Header h = e.nextElement();
        String name=h.getName();
        String value=h.getValue();
        System.out.println(value);
        System.out.println(name);
        h.getValue();
    }
    System.out.println("String: " + s);
     // parse(s);
      System.out.println();
	  }
  }

}