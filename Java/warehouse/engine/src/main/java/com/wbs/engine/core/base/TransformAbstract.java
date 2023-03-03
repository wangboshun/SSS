package com.wbs.engine.core.base;

import org.springframework.stereotype.Component;

/**
 * @author WBS
 * @date 2023/3/3 15:59
 * @desciption TransformAbstract
 */
@Component
public class TransformAbstract implements ITransform {
    private IWriter writer;
    private IReader reader;

    @Override
    public void config(IReader reader, IWriter writer) {
        this.writer = writer;
        this.reader = reader;
    }

    public void mapper() {

    }
}
