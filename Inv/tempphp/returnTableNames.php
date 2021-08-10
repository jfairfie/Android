<?php

require 'dbConnection.php';

ob_clean();

$sql = "SELECT tablename FROM Whitelist;";
$stmt = mysqli_prepare($conn, $sql);

$returnArray = array();

if (mysqli_execute($stmt)) {
	$result = mysqli_stmt_get_result($stmt);
	while ($row = mysqli_fetch_array($result, MYSQLI_NUM)) {
		foreach ($row as $r) {
			$tempArray = array("tablename" => $r);
				$returnArray[] = $tempArray;	
		}
	}
} else {
	echo "Failed to exeute statement";
}

echo json_encode($returnArray);

mysqli_stmt_close($stmt);
