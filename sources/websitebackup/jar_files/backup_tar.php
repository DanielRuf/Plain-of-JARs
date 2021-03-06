<?php
ini_set('memory_limit','#memory_limit#');
ini_set('max_execution_time', 0);
set_time_limit(0);
backup_tables("#db_host#","#db_user#","#db_password#","#db_name#");
function Tar($source, $destination)
{

	if (!extension_loaded('phar') || !file_exists($source)) {
        return false;
    }

    $tar = new PharData($destination);
    if (!Phar::canCompress()) {
        return false;
    }
	
    $source = str_replace('\\', '/', realpath($source));

    if (is_dir($source) === true)
    {
        $files = new RecursiveIteratorIterator(new RecursiveDirectoryIterator($source), RecursiveIteratorIterator::SELF_FIRST);

        foreach ($files as $file)
        {
            $file = str_replace('\\', '/', $file);

            // Ignore "." and ".." folders
            if( in_array(substr($file, strrpos($file, '/')+1), array('.', '..','backup.php','backup','backup.tar','backup.zip', 'backup.tar.gz')) )
                continue;

            $file = realpath($file);

            if (is_dir($file) === true)
            {	
                $tar->addEmptyDir(basename($file));
				$tar[basename($file)]->chmod(substr(sprintf('%o', fileperms($file)), -4));
            }
            else if (is_file($file) === true)
            {
                $tar->addFile($file, basename($file));
				$tar[basename($file)]->chmod(substr(sprintf('%o', fileperms($file)), -4));
            }
        }
    }
    else if (is_file($source) === true)
    {
        $tar->addFile($source, basename($source));
		$tar[basename($source)]->chmod(substr(sprintf('%o', fileperms($source)), -4));
    }

   return $tar->compress(Phar::GZ);
}

//backup_tables('localhost','username','password','*');

/* backup the db OR just a table */
function backup_tables($host,$user,$pass,$name,$tables = '*')
{
	
	$link = mysql_connect($host,$user,$pass);
	mysql_select_db($name,$link);
	
	//get all of the tables
	if($tables == '*')
	{
		$tables = array();
		$result = mysql_query('SHOW TABLES');
		while($row = mysql_fetch_row($result))
		{
			$tables[] = $row[0];
		}
	}
	else
	{
		$tables = is_array($tables) ? $tables : explode(',',$tables);
	}
	
	//cycle through
	foreach($tables as $table)
	{
		$result = mysql_query('SELECT * FROM '.$table);
		$num_fields = mysql_num_fields($result);
		
		$return.= '-- DROP TABLE '.$table.';';
		$row2 = mysql_fetch_row(mysql_query('SHOW CREATE TABLE '.$table));
		$return.= "\n\n".$row2[1].";\n\n";
		
		for ($i = 0; $i < $num_fields; $i++) 
		{
			while($row = mysql_fetch_row($result))
			{
				$return.= 'INSERT INTO '.$table.' VALUES(';
				for($j=0; $j<$num_fields; $j++) 
				{
					$row[$j] = addslashes($row[$j]);
					$row[$j] = ereg_replace("\n","\\n",$row[$j]);
					if (isset($row[$j])) { $return.= '"'.$row[$j].'"' ; } else { $return.= '""'; }
					if ($j<($num_fields-1)) { $return.= ','; }
				}
				$return.= ");\n";
			}
		}
		$return.="\n\n\n";
	}
	
	//save file
	$handle = fopen('backup.sql','w+');
	// fwrite($handle,utf8_encode($return));
	fwrite($handle,utf8_decode($return));
	fclose($handle);
	if(Tar('./', './backup/backup.tar')){unlink("backup/backup.tar"); $handle=fopen("backup/backup_finished.txt", 'w+'); fclose($handle);}
}
?>