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

public class Register extends AppCompatActivity {
    EditText ad, soyad, telefon, mail, sifre;
    static SharedPreferences sp;
    static SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ad = findViewById(R.id.et_ad);
        soyad = findViewById(R.id.et_soyad);
        telefon = findViewById(R.id.et_tel);
        mail = findViewById(R.id.et_mail);
        sifre = findViewById(R.id.et_pass);
        sp=getSharedPreferences("urun", Context.MODE_PRIVATE);
        edit=sp.edit();


    }

    public void KayitOl(View view)
    {
        final String userad=ad.getText().toString();
        final String usersoyad=soyad.getText().toString();
        final String usertelefon=telefon.getText().toString();
        final String userMail=mail.getText().toString();
        final String userSifre=sifre.getText().toString();

        if (ad.getText().toString().equals("") || soyad.getText().toString().equals("") || telefon.getText().toString().equals("") || mail.getText().toString().equals("") || sifre.getText().toString().equals("")) {
            Toast.makeText(this, "Eksik Bilgi Girdiniz", Toast.LENGTH_SHORT).show();

        } else {
         String   url = "http://jsonbulut.com/json/userRegister.php?ref=cb226ff2a31fdd460087fedbb34a6023&" +
                    "userName=" + userad +
                    "&userSurname=" +usersoyad+
                    "&userPhone=" + usertelefon +
                    "&userMail=" +userMail +
                    "&userPass=" + userSifre;

            Log.d("URL",url);
            new jsonRegisterData(url,Register.this).execute();
            Intent intent=new Intent(Register.this
                    ,Profil.class);
            startActivity(intent);

        }
    }
    class jsonRegisterData extends AsyncTask<Void, Void, Void> {

        String url = "";
        String data = "";
        Context cnx;
        ProgressDialog pd;

        public jsonRegisterData(String url, Context cnx) {
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
                if (durum==true ) {
                    //kayıt basarılı
                    Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                    //id çekiyoruz
                    //Object olduğunu süslü parantezden anlıyoruz
                    String kid = obj.getJSONArray("user")
                            .getJSONObject(0)
                            .getJSONObject("bilgiler")
                            .getString("userId");
                    Log.d("kid = ", kid);

                    Register.this.edit.putString("kid",kid);
                    Register.this.edit.commit();




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
}

