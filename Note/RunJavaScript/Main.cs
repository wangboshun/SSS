 static void Main(string[] args)
 {
     using (var engine = new V8ScriptEngine())
     {
         string filePath = "script.js";
         string script = File.ReadAllText(filePath);
         engine.Execute(script);

         engine.Script.a = "a";
         engine.Script.b = "b";
         engine.Script.c = "c";
         var result = engine.Script.getParams(engine.Script.a, engine.Script.b, engine.Script.c);
         Console.WriteLine($"Result: {result["id"]}");
         Console.WriteLine($"Result: {result["token"]}");
     } 
 }