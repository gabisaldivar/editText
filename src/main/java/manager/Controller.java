package main.java.manager;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Window;
import model.Document;
import javafx.stage.FileChooser;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Controller {

    private SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

    @FXML
    private TextArea textContent;

    @FXML
    private TextField textAuthor;

    @FXML
    private TextField textDate;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnLoad;

    @FXML
    private Button btnClean;

    @FXML
    protected void btnLoadFile(ActionEvent activeEvent) {
        Window owner = this.btnLoad.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(owner);
        if(file != null){
            Document document = this.readFile(file);
            this.textAuthor.setText(document.getAuthor());
            this.textContent.setText(document.getContent());
            this.textDate.setText(sdf.format(document.getDate()));
        }else{
            AlertMessage.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Error file not found");
        }
    }

    private Document readFile(File file){
        Document document = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            List<String> list = new ArrayList<>();
            String line;
            while((line = br.readLine()) != null){
                list.add(line);
            }

            document = new Document();
            document.setAuthor(list.get(0));
            document.setContent(list.get(1));
            document.setDate(sdf.parse(list.get(2)));
        }catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return document;
    }


    @FXML
    protected void btnCleanWin(ActionEvent activeEvent) {
        this.textDate.setText("");
        this.textContent.setText("");
        this.textAuthor.setText("");
    }

    @FXML
    protected void btnSaveFile(ActionEvent activeEvent) {
        Window owner = this.btnSave.getScene().getWindow();
        if(textAuthor.getText().isEmpty()) {
            AlertMessage.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter author");
            return;
        }
        if(textContent.getText().isEmpty()) {
            AlertMessage.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter content");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(owner);

        Document document = new Document();
        document.setAuthor(this.textAuthor.getText());
        document.setContent(this.textContent.getText());
        document.setDate(new Date());

        if (file != null) {
            saveTextToFile(document, file, owner);
            this.textDate.setText(sdf.format(document.getDate()));
        }else{
            AlertMessage.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "File error");
            return;
        }

    }
    private void saveTextToFile(Document document, File file, Window owner) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(document.getAuthor());
            writer.println(document.getContent());
            writer.println(sdf.format(document.getDate()));
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            AlertMessage.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Error dont save File");
        }
    }
}
