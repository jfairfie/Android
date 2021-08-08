<?php

require 'dbConnection.php';

$sql = "SELECT tablename FROM Whitelist;";

$result = mysqli_query($conn, $sql);
//$row = mysqli_fetch_array($result);

$whiteList = array();

while ($row = mysqli_fetch_row($result)) {
	$addArray = array("tablename" => $row[0]);
	$whiteList[] = $addArray; 
}

echo json_encode($whiteList);
