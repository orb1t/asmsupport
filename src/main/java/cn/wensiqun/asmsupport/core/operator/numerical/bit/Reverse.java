/**
 * 
 */
package cn.wensiqun.asmsupport.core.operator.numerical.bit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.wensiqun.asmsupport.core.Parameterized;
import cn.wensiqun.asmsupport.core.block.ProgramBlockInternal;
import cn.wensiqun.asmsupport.core.operator.Operators;

/**
 * 
 * @author 温斯群(Joe Wen)
 *
 */
public class Reverse extends UnaryBitwise {

    private static Log log = LogFactory.getLog(Reverse.class);
    
    protected Reverse(ProgramBlockInternal block, Parameterized factor) {
        super(block, factor);
        this.operator = Operators.REVERSE;
    }

    @Override
    public void doExecute() {
        log.debug("start inverts operaotr : " + this.operator);
        log.debug("factor to stack");
        factorToStack();
        log.debug("start invert");
        insnHelper.inverts(targetClass.getType());
    }

}