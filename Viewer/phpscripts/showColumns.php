<?php

require 'dbConnection.php';

ob_clean();

$table = $_POST['table'];

$sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = " . "'$table'" . ";";
$stmt = mysqli_prepare($conn, $sql);

$retList = array();

if (mysqli_stmt_execute($stmt)) {
	$result = mysqli_stmt_get_result($stmt);
	while ($row = mysqli_fetch_array($result, MYSQLI_NUM)) {
		foreach ($row as $r) {
			$tempArray = array("columnname" => $r);
			$retList[] = $tempArray;
		}
	}	
	echo json_encode($retList);
} else {
	echo "selecting columns error";
}
