using Microsoft.Extensions.Diagnostics.HealthChecks;

using System;
using System.Threading;
using System.Threading.Tasks;

namespace SSS.Api.HealthCheck
{
    /// <summary>
    ///     RandomHealthCheck
    /// </summary>
    public class RandomHealthCheck : IHealthCheck
    {
        /// <summary>
        ///     CheckHealthAsync
        /// </summary>
        /// <param name="context"></param>
        /// <param name="cancellationToken"></param>
        /// <returns></returns>
        public Task<HealthCheckResult> CheckHealthAsync(HealthCheckContext context, CancellationToken cancellationToken = default)
        {
            if (DateTime.UtcNow.Minute % 2 == 0) 
				return Task.FromResult(HealthCheckResult.Healthy());

            return Task.FromResult(HealthCheckResult.Unhealthy(description: "failed"));
        }
    }
}