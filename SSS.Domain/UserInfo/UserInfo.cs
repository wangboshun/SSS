using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.UserInfo
{
    public class UserInfo : Entity
    {
        public UserInfo(string id, string UserName, string PassWord)
        {
            this.Id = id;
            this.PassWord = PassWord;
            this.UserName = UserName;
        }


        public string UserName { set; get; }

        public string PassWord { set; get; }
    }
}