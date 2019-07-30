using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.UserInfo
{
    public class UserInfo : Entity
    {
        public UserInfo(string id, string openid, string phone, string name)
        {
            this.Id = id;
            this.Name = name;
            this.Openid = openid;
            this.Phone = phone; 
        }

        /// <summary>
        /// ����
        /// </summary>
        public string Name { set; get; }
         
        /// <summary>
        /// �ֻ���
        /// </summary>
        public string Phone { set; get; }

        /// <summary>
        /// Openid
        /// </summary>
        public string Openid { set; get; }
         

    }
}
