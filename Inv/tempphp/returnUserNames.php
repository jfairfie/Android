<?php

require 'dbConnection.php';

ob_clean();

$sql = "SELECT username FROM User;";
$result = mysqli_query($conn, $sql);
$json = array();

while ($row = mysqli_fetch_row($result)) {
	$tempArray = array('username' => $row[0]);
	$json[] = $tempArray;
}

echo json_encode($json);
