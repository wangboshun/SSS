using Furion;

Serve.Run(RunOptions.Default.WithArgs(args).ConfigureBuilder(builder =>
{
    builder.WebHost.UseUrls($"http://*:{App.GetConfig<int>("Sys:Port")}");
}));