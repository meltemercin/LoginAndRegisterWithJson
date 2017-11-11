package meltemercin.loginandregisterwithjson;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Profil extends AppCompatActivity
{
    static SharedPreferences sp;
    static SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        sp=getSharedPreferences("urun", Context.MODE_PRIVATE);
        edit=sp.edit();        
        

    }

    public void CikisYap(View view)
    {
        AlertDialog.Builder uyari=new AlertDialog.Builder(Profil.this);
        uyari.setTitle("Çıkış Yap");
        uyari.setMessage("Çıkış Yapmak İstediğinize Emin Misiniz?");
        uyari.setCancelable(true);
        //EVET
        uyari.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
              edit.remove("kid");
              if(edit.commit()){
                  Toast.makeText(Profil.this, "Çıkış Yaptınız", Toast.LENGTH_SHORT).show();
                  finish();
              }
            }
        });
        uyari.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Profil.this, "İptal Edildi", Toast.LENGTH_SHORT).show();
            }
        });
        
         AlertDialog alt=uyari.create();
         alt.show();

    }
}
