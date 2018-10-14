package com.example.rick.bikepark;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rick.bikepark.DAO.ConfigFireBase;
import com.example.rick.bikepark.Entidades.Usuarios;
import com.example.rick.bikepark.Helper.Base64Custom;
import com.example.rick.bikepark.Helper.Preferencias;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editSenha;
    private EditText editCOnfirmSenha;
    private EditText editEmail;
    private Button btCadastro;
    private Usuarios usuarios;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editNome = (EditText) findViewById(R.id.nome);
        editSenha = (EditText) findViewById(R.id.senha);
        editCOnfirmSenha = (EditText) findViewById(R.id.senha2);
        editEmail = (EditText) findViewById(R.id.email);
        btCadastro = (Button) findViewById(R.id.btCadastrar);

        btCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editSenha.getText().toString().equals(editCOnfirmSenha.getText().toString())){
                    usuarios = new Usuarios();
                    usuarios.setNome(editNome.getText().toString());
                    usuarios.setSenha(editSenha.getText().toString());
                    usuarios.setEmail(editEmail.getText().toString());

                    cadastrarUsuario();
                } else {
                    Toast.makeText(CadastroActivity.this, "Senhas não confere", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cadastrarUsuario() {
        autenticacao = ConfigFireBase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuarios.getEmail(),
                usuarios.getSenha()).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CadastroActivity.this, "Usuario cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                    String identificadorUsuario = Base64Custom.codificarBase64(usuarios.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuarios.setId(identificadorUsuario);
                    usuarios.salvar();

                    Preferencias preferencias = new Preferencias(CadastroActivity.this);
                    preferencias.salvarUsuario(identificadorUsuario, usuarios.getNome());

                    abrirLoginUsuario();
                } else {

                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthActionCodeException e){
                        erroExcecao = "Digite uma senha mais forte, contendo no minimo 8 caracteres";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "O email digitado é inválido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse email já esta cadastrado no sistema";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, "Erro: " + erroExcecao , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void abrirLoginUsuario() {
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
