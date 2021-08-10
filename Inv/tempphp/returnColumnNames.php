<?php

require 'dbConnection.php';
require 'returnWhiteList.php';

ob_clean();

$category = $_POST['category'];
$retList = array();
$field = "";

for ($i = 0; $i < count($whiteList); $i++) {
	if (strcmp($whiteList[$i]['tablename'], $category) == 0) {	
		//$sql = "SELECT COLUMN_NAME as Field FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = " ."'$dbname'" . " AND TABLE_NAME= " . "'$category'" . " AND COLUMN_KEY='PRI' UNION SELECT COLUMN_NAME AS Field FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA= " . "'$dbname"' . " AND TABLE_NAME=" . "'$category'" . ";"; 		
		$sql = "SELECT COLUMN_NAME as Field FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = " . "'$dbname'" . " AND TABLE_NAME = " . "'$category'" . " ORDER BY ORDINAL_POSITION";		

		$stmt = mysqli_prepare($conn, $sql);
		
		if (mysqli_execute($stmt)) {
			$result = mysqli_stmt_get_result($stmt);

			while ($row = mysqli_fetch_array($result, MYSQLI_NUM)) {
				foreach ($row as $r) {
					$field = $field . $r . ",";
					$tempArray = array("columnname" => $r);
					$retList[] = $tempArray;
				}
			}
			$field = substr_replace($field , "" , -1);
			echo json_encode($retList);
			return;				
		} else {
			echo "failed";
		}
		
	} else {
		echo "error";
	}

}
