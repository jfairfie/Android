<?php

$endpoint="--Enter a endpoint--";
$username=$_POST["username"];
$password=$_POST["password"];
$dbname="--Enter the Database Name--";

//Establish Connection
$conn = mysqli_connect($endpoint, $username, $password, $dbname);

if (!$conn) {
    die("Failed" . mysqli_connect_error());
}

echo "Connection";
?>
