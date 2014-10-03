package br.com.estagio.desafiotagview;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;
import com.google.gson.Gson;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class MainActivity extends Activity {

	// Campos da Activity
	private EditText edt_valor;
	private Spinner spinner;
	private TextView txt_resposta;
	private Button btn_calcular;
	private double valor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.edt_valor = (EditText) findViewById(R.id.edit_valor_brl);
		this.spinner = (Spinner) findViewById(R.id.spinner_conversao);
		this.txt_resposta = (TextView) findViewById(R.id.txt_resposta);
		this.btn_calcular = (Button) findViewById(R.id.btn_calcular);

		
		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		 StrictMode.setThreadPolicy(policy);
		 //habilita a conexao de uma aplicação à internet

	}

	@SuppressLint("ShowToast")
	public void calcular(View v) {
		// Gerar a url de conexão e verificar se o valor passado é válido
		String cabecalhoPagina = gerarURL();
		BufferedReader leitor;
		if (valor >= 0) {
			try {

				// Acessar o servidor com o URL(Como é feitro através do
				// browser)
				URL paginaHTTP = new URL(cabecalhoPagina);
				URLConnection conexao = paginaHTTP.openConnection();

				leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

				String linhaLida = "";
				if (leitor.ready()) {
					linhaLida = leitor.readLine();	//é o conteúdo que a página HTTP responde em String(JSON)
				}

				JSONObject json = new JSONObject(linhaLida); //JSONObject está org.json
				txt_resposta.setText("R$ " + String.valueOf(String.format("%.2f",json.getDouble("v")))); //pegará apenas duas casas decimais do double

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			edt_valor.setText("");
			txt_resposta.setText("R$");
		}
	}

	private String gerarURL() {
		String url = "http://rate-exchange.appspot.com/currency?"; //cabeçalho será terminado no final desta função

		String moeda = ""; //no spinner, será pego as inicias do dinheiro que será convertido (antes do "-")
		moeda = spinner.getSelectedItem().toString().split("-")[0];

		// Capturar o valor a ser convertido
		valor = 0;
		try {
			valor = Integer.parseInt(edt_valor.getText().toString());
		} catch (Exception e) {
			Toast.makeText(this, R.string.valor_inv_lido, Toast.LENGTH_LONG);
			valor = 1;	//se não for um número, calculará como sendo 1
		}

		url += "from=" + moeda + "&to=BRL&q=" + valor;

		return url;
	}

}
