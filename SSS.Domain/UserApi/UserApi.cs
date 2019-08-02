using SSS.Domain.Seedwork.Model;

namespace SSS.Domain.UserApi
{
    public class UserApi : Entity
    {
        public UserApi(string id, string ApiKey, string Secret, string PassPhrase, string UserId)
        {
            this.Id = id;
            this.ApiKey = ApiKey;
            this.Secret = Secret;
            this.PassPhrase = PassPhrase;
            this.UserId = UserId;
        }

        public string ApiKey { set; get; }

        public string Secret { set; get; }

        public int Status { set; get; }

        public string PassPhrase { set; get; }

        public string UserId { set; get; }
    }
}