package com.omerarslan.instaclonefirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ArrayList<String> userEmailFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<String> userImageFromFB;

    FeedRecycleAdapter feedRecycleAdapter;

    ////////////Menü İşlemleri
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Menüyü bağlamak için

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.insta_options_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //Menüde bir şey seçilirse yapılacakları belirtmek için

        if (item.getItemId() == R.id.add_post){
            Intent intentToUpload = new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(intentToUpload);

        } else if (item.getItemId() == R.id.signout){
            firebaseAuth.signOut(); //çıkış yap

            Intent intentToSignUp = new Intent(FeedActivity.this, SignUpActivity.class);
            startActivity(intentToSignUp);
            finish(); //kullanıcı çıkış yaptıktan sonra geri dönemesin
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        userCommentFromFB = new ArrayList<>();
        userEmailFromFB = new ArrayList<>();
        userImageFromFB = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestore();

        //RecycleView

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedRecycleAdapter = new FeedRecycleAdapter(userEmailFromFB, userCommentFromFB, userImageFromFB);
        recyclerView.setAdapter(feedRecycleAdapter);


    }


    ////////////Gerçek zamanlı güncellemeleri gösteren veri çekme işlemi
    public void getDataFromFirestore(){

        CollectionReference collectionReference = firebaseFirestore.collection("Posts"); // Hangi collecitondan veri çekeceğimizi referans verdik

        //Filtreleme işlemi : comment'i Joesph Turner'e eşit olan verileri getir.
        //collectionReference.whereEqualTo("comment", "Joesph Turner").addSnapshotListener(new EventListener<QuerySnapshot>()


                           //Sıralama işlemi   :      order by DESC
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null){
                    Toast.makeText(FeedActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (value != null){
                                                   //value.getDocuments() //snapshotları bize bir liste olarak veriyor.
                    for (DocumentSnapshot snapshot : value.getDocuments()){

                        Map<String, Object> data = snapshot.getData();

                                        //Casting
                        String comment = (String) data.get("comment"); //Value Object tipinde kayıtlı olduğu için String'e cast ettik
                        String userEmail = (String) data.get("useremail");
                        String downloadUrl = (String) data.get("downloadurl");

                        userCommentFromFB.add(comment);
                        userEmailFromFB.add(userEmail);
                        userImageFromFB.add(downloadUrl);

                        feedRecycleAdapter.notifyDataSetChanged(); //içeri yeni veri gelince adapteri uyarmak için

                    }
                }

            }
        });

    }
}