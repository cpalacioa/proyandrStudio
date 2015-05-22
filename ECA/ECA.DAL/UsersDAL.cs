using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ECA.DAL
{
    public class UsersDAL
    {


        public static Boolean GetInfoUser(string email, string password)
        {
            Boolean valid = false;
            ECAEntities db = new ECAEntities();
            string hash = Helpers.EncodePassword(string.Concat(email, password));
            int founduser = db.EC_Users.Where(usu => usu.Email == email && usu.Email == password).Count();
            if (founduser == 1)
                return true;
            else
                return false;

        }

        public static Boolean GetUser(string email)
        {
            ECAEntities db = new ECAEntities();
            int user = db.EC_Users.Where(usu => usu.Email == email).Count();
            if (user > 0)
                return true;
            else
                return false;
        }

        public static Log InsertAdicionalInfoUser(EC_InfoUser infoUser)
        {
            try
            {
                ECAEntities db = new ECAEntities();
                db.EC_InfoUser.Add(infoUser);
                db.SaveChanges();
                return new Log(true, string.Empty);
            }

            catch(Exception ex)
            {
                return new Log(false, ex.ToString());
            }
        }

        public static Log UpdateAdicionalInfoUser(EC_InfoUser _infoUser)
        {
            try
            {
                ECAEntities db = new ECAEntities();
                EC_InfoUser infoUser = db.EC_InfoUser.Where(info => info.IdUsuario == _infoUser.IdUsuario).FirstOrDefault();
                db.EC_InfoUser.Attach(infoUser);
                db.SaveChanges();
                return new Log(true, string.Empty);

            }
            catch(Exception ex)
            {
                return new Log(false, ex.ToString());
            }
        }


        public static Log  InsertUser(string email, string password, string username)
        {
            bool exist = GetUser(email);
            if(exist)
            {
                return new Log(false, "El usuario existe");
            }
            else
            {
                string hash = Helpers.EncodePassword(string.Concat(email, password));
                try
                {
                    ECAEntities db = new ECAEntities();
                    EC_Users user = new EC_Users();
                    user.Email = email;
                    user.Enabled = false;
                    user.password = hash;
                    user.Username = username;
                    db.EC_Users.Add(user);
                    db.SaveChanges();
                    return new Log(true, "Creado correctamente");
                    
                }

                catch(Exception ex)
                {
                    return new Log(false, ex.ToString());
                }
            }
        }


        public static List<Usuario> GetAllUsers()
        {
            ECAEntities db = new ECAEntities();
            List<Usuario> users = new List<Usuario>();
            var userdb = db.EC_Users.ToList();
            foreach (var user in userdb)
            {

                users.Add(new Usuario
                {
                    Apellido = user.EC_InfoUser.Apellidos,
                    Descripcion = user.EC_InfoUser.Description,
                    Email = user.Email,
                    Id = user.Id,
                    Nombre = user.EC_InfoUser.Nombres,
                    Username = user.Username
                });
            }
            return users.ToList();

        }



    }
}
