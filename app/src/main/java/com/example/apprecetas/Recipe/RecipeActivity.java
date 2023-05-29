    package com.example.apprecetas.Recipe;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.content.ContextCompat;

    import android.content.Context;
    import android.content.SharedPreferences;
    import android.graphics.drawable.Drawable;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.example.apprecetas.R;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.QueryDocumentSnapshot;
    import com.google.firebase.firestore.QuerySnapshot;
    import com.squareup.picasso.Picasso;

    import java.util.ArrayList;
    import java.util.Map;


    public class RecipeActivity extends AppCompatActivity {

        private FirebaseFirestore firestore;
        private String titulo;

        private ImageView imagenView;
        private TextView tituloTextView;
        private TextView descripcionTextView;
        private TextView ingredientesTextView;
        private TextView tutorialTextView;
        private TextView caracteristicasTextView;
        private TextView valoresNutricionalesTextView;
        private String recetaNombre;
        private Button addFavoriteButton;
        private boolean esFavorito;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_recipe);
            firestore = FirebaseFirestore.getInstance();

            //obtenemos el titulo del extra que enviamos desde home fragment
            titulo = getIntent().getStringExtra("titulo");

            imagenView = findViewById(R.id.imagen);
            tituloTextView = findViewById(R.id.titulo);
            descripcionTextView = findViewById(R.id.descripcion);
            ingredientesTextView = findViewById(R.id.ingredientes);
            tutorialTextView = findViewById(R.id.tutorial);
            caracteristicasTextView = findViewById(R.id.caracteristicas);
            valoresNutricionalesTextView = findViewById(R.id.valoresNutricionales);

            addFavoriteButton = findViewById(R.id.botonFavoritos);
            SharedPreferences sharedPref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
            esFavorito = sharedPref.getBoolean(recetaNombre, false);
            cambiarBotonFavoritos();
            addFavoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    añadirFavoritos();
                    cambiarBotonFavoritos();
                }
            });
            getDatos();
        }


        private void getDatos() {

            //obtenemos de la coleccion recetas el documento que tiene un  titulo que coincide con el que enviamos desde home fragment
            firestore.collection("recetas").whereEqualTo("titulo", titulo).get().addOnCompleteListener(
                    new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                                //mandamos el documento a la clase recipe para crear un objeto
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                Recipe receta = document.toObject(Recipe.class);
                                recetaNombre = receta.getTitulo();
                                setDatos(receta);
                                cambiarBotonFavoritos();
                            }
                        }
                    });
        }

        private void setDatos(Recipe receta) {

            // con picasso, cargamos la imagen obtenida
            Picasso.get().load(receta.getImagen()).into(imagenView);

            // establecemos el titulo y descripcion
            tituloTextView.setText(receta.getTitulo());
            descripcionTextView.setText(receta.getDescripcion());

            //mediante stringbuilder transformamos la lista a una cadena string normal
            ArrayList<String> ingredientes = receta.getIngredientes();
            if (ingredientes != null) {
                StringBuilder ingredientesText = new StringBuilder();

                for (int i = 0; i < ingredientes.size(); i++) {
                    String ingrediente = ingredientes.get(i);
                    //escribimos antes de cada elemento un simbolo
                    ingredientesText.append("").append(ingrediente);
                    if (i != ingredientes.size() - 1) {
                        //damos un salto de linea en cada elemento menos el ultimo
                        ingredientesText.append("\n");
                    }
                }
                // establecemos el texto al textview de ingredientes
                ingredientesTextView.setText(ingredientesText.toString());
            }

            ArrayList<String> tutoriales = receta.getTutorial();
            if (tutoriales != null) {
                StringBuilder tutorialesText = new StringBuilder();

                for (int i = 0; i < tutoriales.size(); i++) {
                    String tutorial = tutoriales.get(i);
                    tutorialesText.append("⬤  ").append(tutorial);

                    if (i != tutoriales.size() - 1) {
                        tutorialesText.append("\n\n");
                    }
                }
                tutorialTextView.setText(tutorialesText.toString());
            }

            ArrayList<String> caracteristicas = receta.getCaracteristicas();
            if (caracteristicas != null) {
                StringBuilder caracteristicasText = new StringBuilder();
                for (int i = 0; i < caracteristicas.size(); i++) {
                    String caracteristica = caracteristicas.get(i);
                    caracteristicasText.append("⬤  ").append(caracteristica +"  ⬤");
                    if (i != caracteristicas.size() - 1) {
                        caracteristicasText.append("\n");
                    }
                }
                caracteristicasTextView.setText(caracteristicasText.toString());
            }

            Map<String, String> valoresNutricionales = receta.getValores_nutricionales();
            if (valoresNutricionales != null && !valoresNutricionales.isEmpty()) {
                StringBuilder valoresNutricionalesText = new StringBuilder();

                int i = 1;
                for (Map.Entry<String, String> entry : valoresNutricionales.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    valoresNutricionalesText.append("").append(key).append(": ").append(value);

                    if (i != valoresNutricionales.size()) {
                        valoresNutricionalesText.append("\n");
                    }

                    i++;
                }
                valoresNutricionalesTextView.setText(valoresNutricionalesText.toString());
            } else {
                valoresNutricionalesTextView.setText("No hay datos disponibles");
            }
        }

        private void añadirFavoritos() {
            esFavorito = !esFavorito;
            SharedPreferences sharedPref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(recetaNombre, esFavorito);
            editor.apply();
            cambiarBotonFavoritos();

            String mensaje;
            if (esFavorito) {
                mensaje = "Agregada a favoritos";
            } else {
                mensaje = "Eliminada de favoritos";
            }

            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        }

        private void cambiarBotonFavoritos() {
            SharedPreferences sharedPref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
            esFavorito = sharedPref.getBoolean(recetaNombre, false);

            Drawable drawable;
            if (esFavorito) {
                drawable = ContextCompat.getDrawable(this, R.drawable.corazon_roto2);

            } else {
                drawable = ContextCompat.getDrawable(this, R.drawable.corazon);
            }

            if (drawable != null) {
                drawable.setTint(ContextCompat.getColor(this, R.color.white));
                addFavoriteButton.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }
        }
        @Override
        protected void onResume() {
            super.onResume();
            cambiarBotonFavoritos();
        }
    }