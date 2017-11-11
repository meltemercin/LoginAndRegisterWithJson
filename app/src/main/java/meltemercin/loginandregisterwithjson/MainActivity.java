package meltemercin.loginandregisterwithjson;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class MainActivity extends AppCompatActivity
{
    EditText mail,pass;
    String email,sifre;
   static SharedPreferences sp;
    static SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);

sp=getSharedPreferences("urun",Context.MODE_PRIVATE);
edit=sp.edit();
String kulId=sp.getString("kid","");
if(!kulId.equals(""))
{
   Intent pi=new Intent(MainActivity.this,Profil.class);
   pi.putExtra("kulid",kulId);
   startActivity(pi);

}
    }

    public void girisYap(View view)
    {
        email=mail.getText().toString();
        sifre=pass.getText().toString();
     String  url = "http://jsonbulut.com/json/userLogin.php?ref=cb226ff2a31fdd460087fedbb34a6023&" +
                "userEmail=" + email +
                "&userPass=" + sifre +
                "&face=no";
new jsonData(url,MainActivity.this).execute();




    }

    public void kayitYap(View view)
    {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }


}
class jsonData extends AsyncTask<Void, Void, Void> {

    String url = "";
    String data = "";
    Context cnx;
    ProgressDialog pd;

    public jsonData(String url, Context cnx) {
        this.url = url;
        this.cnx = cnx;
        pd = new ProgressDialog(cnx);
        pd.setMessage("İşlem gerçekleşiyor lütfen bekleyiniz");
        pd.show();
    }

    //
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //
    @Override
    protected Void doInBackground(Void... params) {

        try
        {
            data = Jsoup.connect(url).ignoreContentType(true).get().body().text();
        } catch (Exception ex)
        {
            Log.e("data json hatası", "doınBackground", ex);
        }


        return null;
    }

    //
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        //grafiksel ozelliği olan işlemler bu bolumde yapılır
        Log.d("gelen data : ", data);
        try {
            JSONObject obj = new JSONObject(data);
            //çünkü durum boolean bir deger
            boolean durum = obj.getJSONArray("user").getJSONObject(0).getBoolean("durum");
            String mesaj = obj.getJSONArray("user").getJSONObject(0).getString("mesaj");
            if (durum == true) {
                //kayıt basarılı
                Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                //id çekiyoruz
                //Object olduğunu süslü parantezden anlıyoruz
                String kid = obj.getJSONArray("user")
                        .getJSONObject(0)
                        .getJSONObject("bilgiler")
                        .getString("userId");
                Log.d("kid = ", kid);
                MainActivity.edit.putString("kid",kid);
MainActivity.edit.commit();



Intent intent=new Intent(cnx,Profil.class);
cnx.startActivity(intent);
            } else {
                //kayıt basarısız
                Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //yükleme ekranını tamamla(progressbarı kapatıyoruz)
        pd.dismiss();
    }
}
