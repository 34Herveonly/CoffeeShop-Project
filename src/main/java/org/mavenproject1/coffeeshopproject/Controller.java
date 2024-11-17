package org.mavenproject1.coffeeshopproject;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private TextField fp_Answer;

    @FXML
    private ComboBox<?> fp_Question;

    @FXML
    private Button fp_backBtn;

    @FXML
    private Button fp_proceedBtn;

    @FXML
    private AnchorPane fp_questionForm;

    @FXML
    private Button np_backBtn;

    @FXML
    private Button np_changePasswordBtn;

    @FXML
    private PasswordField np_confirmPassword;

    @FXML
    private PasswordField np_newPassword;

    @FXML
    private AnchorPane np_newPasswordForm;

    @FXML
    private Button si_LoginBtn;

    @FXML
    private Hyperlink si_forgotPass;

    @FXML
    private AnchorPane si_loginForm;

    @FXML
    private PasswordField si_password;

    @FXML
    private TextField si_username;

    @FXML
    private Button side_alreadyHave;

    @FXML
    private Button side_createBtn;

    @FXML
    private AnchorPane side_form;

    @FXML
    private TextField su_Answer;

    @FXML
    private ComboBox<?> su_Question;

    @FXML
    private Button su_createBtn;

    @FXML
    private PasswordField su_password;

    @FXML
    private AnchorPane su_signupForm;

    @FXML
    private TextField su_username;

    @FXML
    private TextField fp_username;

    private Alert alert;

    private Connection connect;
    private ResultSet result;
    private PreparedStatement prepare;

    @FXML
    private void loginBtn(ActionEvent event){
        if (si_username.getText().isEmpty() || si_password.getText().isEmpty()) {

            alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Missing Username/Password");
            alert.showAndWait();
        }
        else {
            String selectData="SELECT username,password FROM employees WHERE username=? AND password=?";

            connect=Database.connectDb();

            try{
                prepare=connect.prepareStatement(selectData);
                prepare.setString(1,si_username.getText());
                prepare.setString(2,si_password.getText());

                result=prepare.executeQuery();

                if(result.next()){

                    alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("You have successfully logged in");
                    alert.showAndWait();
                }
                else {
                    alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username/Password");
                    alert.showAndWait();
                }

            }catch (Exception e){e.printStackTrace();}
        }
    }

    @FXML
    private void regBtn(){

        if (su_username.getText().isEmpty() || su_password.getText().isEmpty()||
        su_Question.getSelectionModel().getSelectedItem() == null || su_Answer.getText().isEmpty()){

        alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Please fill all the fields");
        alert.showAndWait();

        }
        else {
            String regData="insert into employees(username,password,question,answer,date)" + " values(?,?,?,?,?)";
            connect=Database.connectDb();

            try{
                String checkUsername="SELECT username FROM employees WHERE username='"+su_username.getText()+"'";

                prepare=connect.prepareStatement(checkUsername);
                result=prepare.executeQuery();

                if (result.next()){
                    alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(su_username.getText()+" already exists");
                    alert.showAndWait();

                }else if (su_password.getText().length()<8){
                    alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Password must be 8 characters or more.") ;
                    alert.showAndWait();

                }else {

                    prepare=connect.prepareStatement(regData);
                    prepare.setString(1,su_username.getText());
                    prepare.setString(2,su_password.getText());
                    prepare.setString(3,su_Question.getSelectionModel().getSelectedItem().toString());
                    prepare.setString(4,su_Answer.getText());

                    java.util.Date date = new java.util.Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setDate(5, sqlDate); // directly set the SQL Date

                    prepare.executeUpdate();

                    alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("You have successfully registered!");
                    alert.showAndWait();

                    su_username.setText("");
                    su_password.setText("");
                    su_Question.getSelectionModel().selectFirst();
                    su_Answer.setText("");

                    TranslateTransition slider=new TranslateTransition();

                    slider.setNode(side_form);
                    slider.setToX(300);
                    slider.setDuration(Duration.seconds(.5));


                    slider.setOnFinished((ActionEvent e)->{
                        side_alreadyHave.setVisible(true);
                        side_createBtn.setVisible(false);

                        regLQuestionList();
                    });


                }

            }catch (Exception e){e.printStackTrace();}


        }

    }


    private String[] questionList={"What is your favorite color?","Who is your favorite Actor?","What is your middle name?","what is your favorite car brand?"};


    public void regLQuestionList(){
        List<String> listQ=new ArrayList<String>();

        for(int i=0;i<questionList.length;i++){
            listQ.add(questionList[i]);
        }
        ObservableList listData= FXCollections.observableArrayList(listQ);
        su_Question.setItems(listData);
    }


    public void switchForgotPassword(){
        fp_questionForm.setVisible(true);
        si_loginForm.setVisible(false);

        forgotPassQuestionList();
    }

    public void proceedBtn() {

        if(fp_username.getText().isEmpty() ||fp_Question.getSelectionModel().getSelectedItem()==null||fp_Answer.getText().isEmpty()){
            alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill the fields");
            alert.showAndWait();
        }
        else {
            String selectData="SELECT username,question,answer FROM employees WHERE username=? AND question=? AND answer=?";
            connect=Database.connectDb();

            try{
            prepare=connect.prepareStatement(selectData);
            prepare.setString(1,fp_username.getText());
            prepare.setString(2,(String) fp_Question.getSelectionModel().getSelectedItem().toString());
            prepare.setString(3,fp_Answer.getText());

            result=prepare.executeQuery();

            if(result.next()){
                np_newPasswordForm.setVisible(true);
                fp_questionForm.setVisible(false);

            }else {
                alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect Credentials");
                alert.showAndWait();
            }

            } catch (Exception e){e.printStackTrace();}
        }
    }

    public void changePassword(){
        if (np_newPassword.getText().isEmpty() || np_confirmPassword.getText().isEmpty()){
            alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill the fields");
            alert.showAndWait();
        }
        else {

                if (np_newPassword.getText().equals(np_confirmPassword.getText())){

                    String getDate="SELECT date FROM employees WHERE username='"+fp_username.getText()+"'";
                    connect=Database.connectDb();

                    try{
                        prepare=connect.prepareStatement(getDate);
                        result=prepare.executeQuery();

                        String date="";

                        if(result.next()){
                            date = result.getString("date");
                        }

                        String updateData="UPDATE employees SET password='"+np_newPassword.getText()+"',question='" +fp_Question
                                .getSelectionModel().getSelectedItem()+"',answer='"+fp_Answer.getText()+"',date='"+ date +"' WHERE username='"+fp_username.getText()+"'";

                            prepare=connect.prepareStatement(updateData);
                            prepare.executeUpdate();

                            alert=new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Successfully changed password");
                            alert.showAndWait();


                            si_loginForm.setVisible(true);
                            np_newPassword.setVisible(false);


                            np_confirmPassword.setText("");
                            np_newPassword.setText("");
                            fp_Question.getSelectionModel().clearSelection();
                            fp_Answer.setText("");
                            fp_username.setText("");


                    }catch (Exception e){e.printStackTrace();}

                }else {
                    alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Password Not matching");
                    alert.showAndWait();

                }

        }
    }


    public void forgotPassQuestionList(){
        List<String> listQ= new ArrayList<>();

        for(String data:questionList){
            listQ.add(data);
        }
        ObservableList listData= FXCollections.observableArrayList(listQ);
        fp_Question.setItems(listData);

     }

        public void backToLoginForm(){
            si_loginForm.setVisible(true);
            fp_questionForm.setVisible(false);

            }

        public void backToQuestionForm(){
        fp_questionForm.setVisible(true);
        np_newPasswordForm.setVisible(false);

        }


    public void switchForm(ActionEvent event) {
        TranslateTransition slider=new TranslateTransition();

        if (event.getSource()==side_createBtn){
            slider.setNode(side_form);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.5));


            slider.setOnFinished((ActionEvent e)->{
                side_alreadyHave.setVisible(true);
                side_createBtn.setVisible(false);


                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPasswordForm.setVisible(false);

                regLQuestionList();
            });
            slider.play();
        } else if (event.getSource()==side_alreadyHave) {
            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e)->{
                side_alreadyHave.setVisible(false);
                side_createBtn.setVisible(true);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPasswordForm.setVisible(false);
            });
            slider.play();
        }
    }

}