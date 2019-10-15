using Hangfire.Annotations;
using Hangfire.Dashboard;

namespace SSS.Api.Seedwork.Filter
{
    /// <summary>
    ///     处理Hangfire 401问题
    /// </summary>
    public class CustomAuthorizeFilter : IDashboardAuthorizationFilter
    {
        public bool Authorize([NotNull] DashboardContext context)
        {
            //var httpcontext = context.GetHttpContext();
            //return httpcontext.User.Identity.IsAuthenticated;
            return true;
        }
    }
}