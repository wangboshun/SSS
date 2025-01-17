df2 = df[df['val'].notna()]
df.loc[df['val'] > max, "status"] =1
df.loc[df['val'] < min, "status"] =2
df["diff"] = df[df['val'].notna()]['val'].diff().abs()
