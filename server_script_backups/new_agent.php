<?php
include_once './DbConnect.php';
function createNewAgent() {
         $response = array();
        $agent = $_POST["agent"];
        $email = $_POST["email"];
        $pass = $_POST["pass"];
                $db = new DbConnect();
       // mysql query
        $query = "INSERT INTO basic_user_info (NAME, EMAIL, PASS) VALUES('$agent','$email', '$pass)";
        $result = mysql_query($query) or die(mysql_error());
        if ($result) {
            $response["error"] = false;
            $response["message"] = "Prediction added successfully!";
        } else {
            $response["error"] = true;
            $response["message"] = "Failed to add prediction!";
        }
       // echo json response
    echo json_encode($response);
}
createNewAgent();
?>

