
package com.example.activent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

//import com.example.activent.TimeTagger.SUTimeParsingError;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListThreadsResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.Thread;
/**
 * Display personalized greeting. This class contains boilerplate code to consume the token but
 * isn't integral to getting the tokens.
 */
public abstract class AbstractGetNameTask extends AsyncTask<Void, Void, Void>{
	private static final String TAG = "TokenInfoTask";
	private static final String NAME_KEY = "given_name";
	protected HelloActivity mActivity;

	protected String mScope;
	static List<Thread> threads=null;
	protected String mEmail;
static ArrayList<String> mail_data=new ArrayList<String>();
	AbstractGetNameTask(HelloActivity activity, String email, String scope) {
		this.mActivity = activity;
		this.mScope = scope;
		this.mEmail = email;
	}
	Gmail mailService;
	@Override
	protected Void doInBackground(Void... params) {
		try {
			fetchNameFromProfileServer();
			GoogleCredential credential = new GoogleCredential()
			.setAccessToken(fetchToken());
			HttpTransport httpTransport = new NetHttpTransport();
			JsonFactory jsonFactory = new JacksonFactory();
			 mailService = new Gmail.Builder(httpTransport, jsonFactory,
					credential).setApplicationName("Activent").build();
			ListThreadsResponse threadsResponse;
			 threads = null;
			try {
				threadsResponse = mailService.users().threads().list("me").setMaxResults(Long.parseLong("4"))
						.execute();
				threads = threadsResponse.getThreads();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("hello"+threads);
			/*String 
			
			threads.toArray(array);*/
			for(Thread thread:threads)
			{
				getMessage(mailService, "me", thread.get("id").toString());
				
			}
			//System.out.println(threads.get(0).get("id"));
			//System.out.print(mail_data);
			TimeTagger tg=new TimeTagger();
			try {
				tg.run_me(mail_data);
			
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//getMessage(mailService, "me", threads.get(0).get("id").toString());
			/*try {
				//getMimeMessage(mailService, "me", threads.get(0).get("id").toString());
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			//batchrequest(mailService);
			//return threads;
		} catch (IOException ex) {
			onError("Following Error occured, please try again. " + ex.getMessage(), ex);
		} catch (JSONException e) {
			onError("Bad response: " + e.getMessage(), e);
		}
		return null;
	}
	String accountName;
	//Gmail mailService;
	ListView threadListView;
	//ArrayList<EmailThreadObject> mailThreads;
	ProgressBar progressBar;
	OnItemClickListener listener;
	Thread currentThread;
	SharedPreferences sharedPrefs;
	String prefsName;
	//ListMailThreadsAdapter threadAdapter;
	
	/*
	public void generateBatch(List<Thread> threadsList) {
		BatchRequest b = mailService.batch();
		mailThreads = new ArrayList<EmailThreadObject>();
		threadAdapter = new ListMailThreadsAdapter(getActivity(), mailThreads);
		threadListView.setAdapter(threadAdapter);
		JsonBatchCallback<Thread> bc = new JsonBatchCallback<Thread>() {

			@Override
			public void onSuccess(Thread t, HttpHeaders responseHeaders)
					throws IOException {
				int mailCount = t.getMessages().size();
				EmailThreadObject thread = new EmailThreadObject();
				if (mailCount > 1) {
					thread.setMailCount(" (" + String.valueOf(mailCount) + ") ");
				} else {
					thread.setMailCount("");
				}
				thread.setMessages(t.getMessages());
				thread.setId(t.getId());
				List<MessagePartHeader> headerFirst = t.getMessages().get(0)
						.getPayload().getHeaders();
				Log.d("processsing", t.getId());
				if (mailCount > 1) {
					List<MessagePartHeader> headerLast = t.getMessages()
							.get(mailCount - 1).getPayload().getHeaders();
					String senderFirst = null, senderLast, sender = null;
					for (MessagePartHeader m : headerFirst) {
						if (m.getName().equals("From")) {
							senderFirst = m.getValue();
							sender = senderFirst;
							if (senderFirst.contains(" ")) {
								senderFirst = senderFirst.substring(0,
										senderFirst.indexOf(' '));
							}
						} else if (m.getName().equals("Subject")) {
							thread.setMailSnippet(m.getValue());
						}
					}
					for (MessagePartHeader m : headerLast) {
						if (m.getName().equals("From")) {
							senderLast = m.getValue();
							if (senderLast.contains(" ")) {
								senderLast = senderLast.substring(0,
										senderLast.indexOf(' '));
							}
							if (!senderFirst.equals(senderLast)) {
								sender = senderFirst + "..." + senderLast;
							}
							Log.d("sender", sender);
							thread.setMailSender(sender);
						} else if (m.getName().equals("Date")) {
							thread.setMailTime(Utilities.getTimeForGmail(m
									.getValue()));
						}
					}
				} else {
					for (MessagePartHeader m : headerFirst) {
						if (m.getName().equals("From")) {
							thread.setMailSender(m.getValue());
						} else if (m.getName().equals("Date")) {
							thread.setMailTime(Utilities.getTimeForGmail(m
									.getValue()));
						} else if (m.getName().equals("Subject")) {
							thread.setMailSnippet(m.getValue());
						}
					}
				}
				mailThreads.add(thread);
			}

			@Override
			public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders)
					throws IOException {

			}
		};

		for (Thread thread : threadsList) {
			try {
				mailService.users().threads().get("me", thread.getId())
						.queue(b, bc);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		new getThreads().execute(b);
	}
	*/
	
	 public static Message getMessage(Gmail service, String userId, String messageId)
		      throws IOException {
		    Message message = service.users().messages().get(userId, messageId).setFormat("raw").execute();
		 
		  // System.out.println(message.getRaw());
		 //   System.out.println(message.getPayload().getHeaders()..getValue()+"####"+message.getPayload().getHeaders().get(37).getValue());
		   String s=new String(Base64.decode(message.getRaw(),Base64.URL_SAFE));
		   // Base64.getDecoder().decode(message.getRaw().toString());
		   
		   
		 //  System.out.println(s);
		   mail_data.add(s);
		   // System.out.println(base64UrlDecode(message.getRaw().toString()));
		    return message;
		  }
	
	 public static void batchrequest(Gmail service)
	 {
		 BatchRequest b = service.batch();
		//callback function. (Can also define different callbacks for each request, as required)
		JsonBatchCallback<Thread> bc = new JsonBatchCallback<Thread>() {

		    @Override
		    public void onSuccess(Thread t, HttpHeaders responseHeaders)
		            throws IOException {
		    //	String s=Base64.decode(t.getMessages().toString(),Base64.URL_SAFE).toString();
		    //	System.out.println(s);
		    //  System.out.println(t.getMessages());
		      //mail_data.add(s);
		    	
		    	  System.out.println("heee"+t.getMessages());
		    }

		    @Override
		    public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders)
		            throws IOException {

		    }
		};

		// queuing requests on the batch requests
		for (Thread thread : threads) {
		    try {
				service.users().threads().get("me", thread.getId()).setFormat("raw").queue(b, bc);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}


		try {
			b.execute();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	 }
	 
	protected void onError(String msg, Exception e) {
		if (e != null) {
			Log.e(TAG, "Exception: ", e);
		}
		mActivity.show(msg);  // will be run in UI thread
	}
	
	/**
	 * Get a authentication token if one is not available. If the error is not recoverable then
	 * it displays the error message on parent activity.
	 */
	protected abstract String fetchToken() throws IOException;

	/**
	 * Contacts the user info server to get the profile of the user and extracts the first name
	 * of the user from the profile. In order to authenticate with the user info server the method
	 * first fetches an access token from Google Play services.
	 * @throws IOException if communication with user info server failed.
	 * @throws JSONException if the response from the server could not be parsed.
	 */
	private void fetchNameFromProfileServer() throws IOException, JSONException {
		String token = fetchToken();
		if (token == null) {
			// error has already been handled in fetchToken()
			return;
		}
		URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + token);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		int sc = con.getResponseCode();
		if (sc == 200) {
			InputStream is = con.getInputStream();
			String name = getFirstName(readResponse(is));
			mActivity.show("Hello " + name + "!");
			is.close();
			return;
		} else if (sc == 401) {
			GoogleAuthUtil.invalidateToken(mActivity, token);
			onError("Server auth error, please try again.", null);
			Log.i(TAG, "Server auth error: " + readResponse(con.getErrorStream()));
			return;
		} else {
			onError("Server returned the following error code: " + sc, null);
			return;
		}
	}

	/**
	 * Reads the response from the input stream and returns it as a string.
	 */
	private static String readResponse(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] data = new byte[2048];
		int len = 0;
		while ((len = is.read(data, 0, data.length)) >= 0) {
			bos.write(data, 0, len);
		}
		return new String(bos.toByteArray(), "UTF-8");
	}

	/**
	 * Parses the response and returns the first name of the user.
	 * @throws JSONException if the response is not JSON or if first name does not exist in response
	 */
	private String getFirstName(String jsonResponse) throws JSONException {
		JSONObject profile = new JSONObject(jsonResponse);
		return profile.getString(NAME_KEY);
	}
}
