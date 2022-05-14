package scm.cptam3.app_1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    // 1 for sword; 2 for bow; 3 for shield
    ArrayList<Integer> my_choices = new ArrayList<Integer>();
    ArrayList<Integer> pc_choices = new ArrayList<Integer>();

   private int my_hp=15;
   private int pc_hp=15;

    @Override
    protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game);

       Button btn_sword = findViewById(R.id.btn_sword);

       btn_sword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (my_choices.size() < 4) {
                   my_choices.add(1);
                   int pc_choice = set_pc_choice();
                   pc_choices.add(pc_choice);
                   int i = my_choices.size();
                   int j = pc_choices.size();
                   set_myimage(i, 1);
                   set_pcimage(j, pc_choice);
               }
           }
       });

       Button btn_bow = findViewById(R.id.btn_bow);

       btn_bow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (my_choices.size() < 4) {
                   my_choices.add(2);
                   int pc_choice = set_pc_choice();
                   pc_choices.add(pc_choice);
                   int i = my_choices.size();
                   int j = pc_choices.size();
                   set_myimage(i, 2);
                   set_pcimage(j, pc_choice);
               }
           }
       });

       Button btn_shield = findViewById(R.id.btn_shield);

       btn_shield.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (my_choices.size() < 4) {
                   my_choices.add(3);
                   int pc_choice = set_pc_choice();
                   pc_choices.add(pc_choice);
                   int i = my_choices.size();
                   int j = pc_choices.size();
                   set_myimage(i, 3);
                   set_pcimage(j, pc_choice);
               }
           }
       });



                ImageButton btn_battle = findViewById(R.id.btn_battle);
                btn_battle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (my_choices.size() == 4) {
                            int my_sword = 0;
                            int my_bow = 0;
                            int my_shield = 0;
                            int pc_sword = 0;
                            int pc_bow = 0;
                            int pc_shield = 0;

                            for (int i = 0; i < 4; i++) {
                                if (my_choices.get(i) == 1) {
                                    my_sword++;
                                }
                                if (pc_choices.get(i) == 1) {
                                    pc_sword++;
                                }
                                if (my_choices.get(i) == 2) {
                                    my_bow++;
                                }
                                if (pc_choices.get(i) == 2) {
                                    pc_bow++;
                                }
                                if (my_choices.get(i) == 3) {
                                    my_shield++;
                                }
                                if (pc_choices.get(i) == 3) {
                                    pc_shield++;
                                }
                            }

                            int my_damage = my_damage_judgement(my_sword, my_bow, pc_shield);
                            int pc_damage = pc_damage_judgement(pc_sword, pc_bow, my_shield);
                            my_hp -= pc_damage;
                            pc_hp -= my_damage;

                            TextView text_myhp = findViewById(R.id.my_hp);
                            text_myhp.setText("hp " + my_hp);
                            TextView text_pchp = findViewById(R.id.computer_hp);
                            text_pchp.setText("hp " + pc_hp);
                            my_choices.clear();
                            pc_choices.clear();
                            reset_image();

                            boolean Notdraw = true;
                            if (my_hp <= 0 && pc_hp <= 0) {
                                Notdraw = false;
                                my_hp = 15;
                                pc_hp = 15;
                                text_myhp.setText("hp " + my_hp);
                                text_pchp.setText("hp " + pc_hp);
                                Toast.makeText(GameActivity.this, "Draw", Toast.LENGTH_SHORT).show();
                            }
                            if (my_hp <= 0 && Notdraw) {
                                my_hp = 15;
                                pc_hp = 15;
                                text_myhp.setText("hp " + my_hp);
                                text_pchp.setText("hp " + pc_hp);
                                Toast.makeText(GameActivity.this, "You lose", Toast.LENGTH_SHORT).show();
                            }
                            if (pc_hp <= 0 && Notdraw) {
                                my_hp = 15;
                                pc_hp = 15;
                                text_myhp.setText("hp " + my_hp);
                                text_pchp.setText("hp " + pc_hp);
                                Toast.makeText(GameActivity.this, "You win", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                });


    }



    protected int set_pc_choice(){

        Random r = new Random();
        int  pc_choice = r.nextInt(3)+1;
        return pc_choice;
    }

    protected void set_myimage(int i, int choice){
        ImageView my_choice1 = findViewById(R.id.img_mychoice1);
        ImageView my_choice2 = findViewById(R.id.img_mychoice2);
        ImageView my_choice3 = findViewById(R.id.img_mychoice3);
        ImageView my_choice4 = findViewById(R.id.img_mychoice4);

        if(i == 1){
            if(choice == 1){
                my_choice1.setImageResource(R.drawable.sword);
            }
            if(choice == 2){
                my_choice1.setImageResource(R.drawable.bow);
            }
            if(choice == 3){
                my_choice1.setImageResource(R.drawable.shield);
            }
        } else if(i == 2){
            if(choice == 1){
                my_choice2.setImageResource(R.drawable.sword);
            }
            if(choice == 2){
                my_choice2.setImageResource(R.drawable.bow);
            }
            if(choice == 3){
                my_choice2.setImageResource(R.drawable.shield);
            }
        } else if(i == 3){
            if(choice == 1){
                my_choice3.setImageResource(R.drawable.sword);
            }
            if(choice == 2){
                my_choice3.setImageResource(R.drawable.bow);
            }
            if(choice == 3){
                my_choice3.setImageResource(R.drawable.shield);
            }
        } else if(i == 4){
            if(choice == 1){
                my_choice4.setImageResource(R.drawable.sword);
            }
            if(choice == 2){
                my_choice4.setImageResource(R.drawable.bow);
            }
            if(choice == 3){
                my_choice4.setImageResource(R.drawable.shield);
            }
        }
    }

    protected void set_pcimage(int j, int choice){
        ImageView pc_choice1 = findViewById(R.id.img_pcchoice1);
        ImageView pc_choice2 = findViewById(R.id.img_pcchoice2);
        ImageView pc_choice3 = findViewById(R.id.img_pcchoice3);
        ImageView pc_choice4 = findViewById(R.id.img_pcchoice4);

        if(j == 1){
            if(choice == 1){
                pc_choice1.setImageResource(R.drawable.sword);
            }
            if(choice == 2){
                pc_choice1.setImageResource(R.drawable.bow);
            }
            if(choice == 3){
                pc_choice1.setImageResource(R.drawable.shield);
            }
        } else if(j == 2){
            if(choice == 1){
                pc_choice2.setImageResource(R.drawable.sword);
            }
            if(choice == 2){
                pc_choice2.setImageResource(R.drawable.bow);
            }
            if(choice == 3){
                pc_choice2.setImageResource(R.drawable.shield);
            }
        } else if(j == 3){
            if(choice == 1){
                pc_choice3.setImageResource(R.drawable.sword);
            }
            if(choice == 2){
                pc_choice3.setImageResource(R.drawable.bow);
            }
            if(choice == 3){
                pc_choice3.setImageResource(R.drawable.shield);
            }
        } else if(j == 4){
            if(choice == 1){
                pc_choice4.setImageResource(R.drawable.sword);
            }
            if(choice == 2){
                pc_choice4.setImageResource(R.drawable.bow);
            }
            if(choice == 3){
                pc_choice4.setImageResource(R.drawable.shield);
            }
        }
    }

    protected int my_damage_judgement(int my_sword, int my_bow, int pc_shield){
        int my_damage=0;
        while(my_bow>0) {
            if(pc_shield==0){
                my_damage++;
                my_bow--;
            }

            if (pc_shield > 0) {
                pc_shield--;
                my_bow--;
            }

        }
        while(my_sword>0){
            if(pc_shield>0){
                break;
            }
            my_damage+=2;
            my_sword--;
        }
        return my_damage;
    }

    protected int pc_damage_judgement(int pc_sword, int pc_bow, int my_shield){
        int pc_damage=0;
        while(pc_bow>0) {
            if(my_shield==0){
                pc_damage++;
                pc_bow--;
            }
            if (my_shield > 0) {
                my_shield--;
                pc_bow--;
            }

        }
        while(pc_sword>0){
            if(my_shield>0){
                break;
            }
            pc_damage+=2;
            pc_sword--;
        }
        return pc_damage;
    }

    protected void reset_image(){
        ImageView my_choice1 = findViewById(R.id.img_mychoice1);
        ImageView my_choice2 = findViewById(R.id.img_mychoice2);
        ImageView my_choice3 = findViewById(R.id.img_mychoice3);
        ImageView my_choice4 = findViewById(R.id.img_mychoice4);
        ImageView pc_choice1 = findViewById(R.id.img_pcchoice1);
        ImageView pc_choice2 = findViewById(R.id.img_pcchoice2);
        ImageView pc_choice3 = findViewById(R.id.img_pcchoice3);
        ImageView pc_choice4 = findViewById(R.id.img_pcchoice4);

        my_choice1.setImageResource(R.drawable.questionmark);
        my_choice2.setImageResource(R.drawable.questionmark);
        my_choice3.setImageResource(R.drawable.questionmark);
        my_choice4.setImageResource(R.drawable.questionmark);
        pc_choice1.setImageResource(R.drawable.questionmark);
        pc_choice2.setImageResource(R.drawable.questionmark);
        pc_choice3.setImageResource(R.drawable.questionmark);
        pc_choice4.setImageResource(R.drawable.questionmark);
    }
}
