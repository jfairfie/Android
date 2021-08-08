<?php

require 'dbConnection.php';
require 'getColumnNames.php';
ob_clean();


$sql = "SELECT " . $category . "." . $field . ",SUM(quantity) AS quantity FROM " . $category . " INNER JOIN Category ON Category." . strtolower($category) . "id" . " = " . $category . "." . strtolower($category) . "id" . " INNER JOIN Entry ON Entry.id = Category.id GROUP BY " . $category . "." . strtolower($category) . "id;";

$stmt = mysqli_prepare($conn, $sql); 


if (mysqli_execute($stmt)) {
	//Delimeter, making an array of field names
	$field = explode(",", $field);
	array_push($field, "quantity");
	$returnField = array();
	
	//Displaying results from sql query
	$result = mysqli_stmt_get_result($stmt);
	while ($row = mysqli_fetch_array($result, MYSQLI_NUM)) {
		$addArray = array();
		for ($i = 0; $i < count($field); $i++) {
			$tempArray = array($field[$i] => $row[$i]);
			$addArray = array_merge($addArray, $tempArray);	
			
		}
		$returnField[] = $addArray;
	}
	echo json_encode($returnField);

} else {
	echo "failed";
}
