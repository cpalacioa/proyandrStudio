<?php
 
/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 *
 */
class DbHandler {
 
    private $conn;
 
    function __construct() {
        require_once dirname(__FILE__) . './DbConnect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }
 
    /* ------------- `users` table method ------------------ */
    public function createUser($username, $email, $aplicacion,$proveedorIdentidad) {
        $response = array();
 
        // First check if user already existed in db
        if (!$this->isUserExists($aplicacion,$proveedorIdentidad,$email)) {
            // insert query
            $stmt = $this->conn->prepare("INSERT INTO usuario(username, email, IdAplicacion, IdProveedorIdentidad) values(?, ?, ?, ?)");
            $stmt->bind_param("ssss", $username, $email, $aplicacion, $proveedorIdentidad);
 
            $result = $stmt->execute();
 
            $stmt->close();
 
            // Check for successful insertion
            if ($result) {
                var_dump($result);
                // User successfully inserted
                return USER_CREATED_SUCCESSFULLY;
            } else {
                // Failed to create user
                return USER_CREATE_FAILED;
            }
        } else {
            // User with same email already existed in the db
            return USER_ALREADY_EXISTED;
        }
 
        return $response;
    }
    
    
    public function InsertDevice($serial,$aplication,$user)
    {
        $response = array();
        $db = new DbHandler();           
        
        if (!$this->isDeviceExists($aplication,$user)) {

            $stmt = $this->conn->prepare("INSERT INTO dispositivo(serial, IdAplicacion, IdUsuario) values(?, ?, ?)");
            var_dump($stmt);
            $stmt->bind_param("sii", $serial, $aplication, $user);
            $result = $stmt->execute();
            $stmt->close();
 
            // Check for successful insertion
            if ($result) {
                // User successfully inserted
                return SUCCESSFULLY;
            } else {
                // Failed to create user
                return FAILED;
            }
        } else {
          //Se debe acualizar
            //UpdateDevice($serial,$aplication,$user);
          $result =$db->UpdateDevice($serial,$aplication,$user);
          if($result)
          {
              return SUCCESSFULLY;
          }
        }
 
        return $response;
        
    }

    public function UpdateDevice($serial,$aplication,$user)
    {
        $stmt = $this->conn->prepare("UPDATE  dispositivo d set  d.serial = ? WHERE d.IdAplicacion=? AND d.IdUsuario=?");
        $stmt->bind_param("sii", $serial,$aplication,$user);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }
    

    private function isUserExists($aplicacion,$proveedor,$email) {
        
        $stmt = $this->conn->prepare("SELECT id from usuario WHERE IdAplicacion = ? && email=? && IdProveedorIdentidad=?");
        $stmt->bind_param("sss",$aplicacion,$email,$proveedor);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }
 
    private function isDeviceExists($aplication,$user)
    {
        $stmt = $this->conn->prepare("SELECT id from dispositivo WHERE IdAplicacion = ? && IdUsuario=?");
        $stmt->bind_param("ii",$aplication,$user);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
         return $num_rows > 0;
        
    }
    
    
    /**
     * Checking user login
     * @param String $email User login email id
     * @param String $password User login password
     * @return boolean User login status success/fail
     */
    public function checkLogin($email, $password) {
        // fetching user by email
        $stmt = $this->conn->prepare("SELECT password_hash FROM users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->bind_result($password_hash);
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // Found user with the email
            // Now verify the password
 
            $stmt->fetch();
 
            $stmt->close();
 
            if (PassHash::check_password($password_hash, $password)) {
                // User password is correct
                return TRUE;
            } else {
                // user password is incorrect
                return FALSE;
            }
        } else {
            $stmt->close();
 
            // user not existed with the email
            return FALSE;
        }
    }
    /**
     * Generating random Unique MD5 String for user Api key
     */
    private function generateApiKey() {
        return md5(uniqid(rand(), true));
    }
 
    /**
     * Fetching single task
     * @param String $task_id id of the task
     */
    public function  getUser($email,$aplicacion,$identityProvider)
    {
        $stmt=$this->conn->prepare("select Id,email,username From usuario where email=? && IdProveedorIdentidad=? && IdAplicacion=?");
        $stmt->bind_param("sss",$email,$identityProvider,$aplicacion);
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }
    
    public function getTask($task_id, $user_id) {
        $stmt = $this->conn->prepare("SELECT t.id, t.task, t.status, t.created_at from tasks t, user_tasks ut WHERE t.id = ? AND ut.task_id = t.id AND ut.user_id = ?");
        $stmt->bind_param("ii", $task_id, $user_id);
        if ($stmt->execute()) {
            $task = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $task;
        } else {
            return NULL;
        }
    }
 
    /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function getAllUserTasks($user_id) {
        $stmt = $this->conn->prepare("SELECT t.* FROM tasks t, user_tasks ut WHERE t.id = ut.task_id AND ut.user_id = ?");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $tasks = $stmt->get_result();
        $stmt->close();
        return $tasks;
    }
 
    /**
     * Deleting a task
     * @param String $task_id id of the task to delete
     */
    public function deleteTask($user_id, $task_id) {
        $stmt = $this->conn->prepare("DELETE t FROM tasks t, user_tasks ut WHERE t.id = ? AND ut.task_id = t.id AND ut.user_id = ?");
        $stmt->bind_param("ii", $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }
 
 
}
 
?>