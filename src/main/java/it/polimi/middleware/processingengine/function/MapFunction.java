package it.polimi.middleware.processingengine.function;

import it.polimi.middleware.processingengine.message.OperateMessage;

public interface MapFunction {

    OperateMessage map(OperateMessage operateMessage);

}
