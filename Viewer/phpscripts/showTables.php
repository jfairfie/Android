<?php

require 'dbConnection.php';

ob_clean();

$sql = "SHOW TABLES;"; 

$stmt = mysqli_prepare($conn, $sql);

$retList = array();

if (mysqli_stmt_execute($stmt)) {
	$result = mysqli_stmt_get_result($stmt);
	
	while ($row = mysqli_fetch_array($result, MYSQLI_NUM)) {
		foreach ($row as $r) {
			$tempArray = array("tablename" => $r);
			$retList[] = $tempArray;	
		}
	}
	echo json_encode($retList);  
} else {
	echo "Error displaying tables";
}

mysqli_stmt_close($stmt);
mysqli_close($conn);
