<?php

//$endpoint="blackpointdatabase.cx7rbplauz8z.us-east-2.rds.amazonaws.com";
$endpoint=$_POST["endpoint"];
$username=$_POST["username"];
$password=$_POST["password"];
$database=$_POST["database"];

//Establish Connection
$conn = mysqli_connect($endpoint, $username, $password);

if (!$conn) {
	echo "401";
} else {

	echo "200";

	if (isset($database)) {
		mysqli_select_db($conn, $database);	
	}
}
?>
