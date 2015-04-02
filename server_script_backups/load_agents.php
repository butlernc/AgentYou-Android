<?php
include_once './DbConnect.php';
function loadAgents() {
	$conn = new mysqli("localhost", "agentyou", "N!221133ncb", "agentyou");
	$query = "SELECT * FROM basic_user_info";
	$result = $conn->query($query) or die(mysql_error());
	$rows = array();
	
	if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()) {
			$rows[] = $row;
		}
	}
	
	echo json_encode($rows);
	
}
loadAgents();
?>
