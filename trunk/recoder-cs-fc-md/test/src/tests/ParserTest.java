package tests;
import junit.framework.*;
import recoder.*;
import recoder.csharp.*;
import recoder.parser.*;
import java.io.*;

/**
 * @author kis
 *
 * Parser Tests.
 */
public class ParserTest extends ParserTestCase {
	final String testDir = "testdata";

	public ParserTest(String name) {
		super(name);
	}

	private void parserTest(String fileName)
		throws IOException, ParserException {
		cu =
			sc.getProgramFactory().parseCompilationUnit(
				new FileReader(fileName));
	}

	public static Test suite() {
		return new TestSuite(ParserTest.class);
	}

	public void testActivator() throws ParserException, IOException {
		parserTest(testDir + "/" + "Activator.cs");
	}

	public void testHello() throws ParserException, IOException {
		parserTest(testDir + "/" + "Hello.cs");
	}

	public void testattributes() throws ParserException, IOException {
		parserTest(testDir + "/" + "attributes.cs");
	}

	public void testclasses() throws ParserException, IOException {
		parserTest(testDir + "/" + "classes.cs");
	}

	public void testcomments() throws ParserException, IOException {
		parserTest(testDir + "/" + "comments.cs");
	}

	public void testhelloworld() throws ParserException, IOException {
		parserTest(testDir + "/" + "helloworld.cs");
	}

	public void testinterfaces() throws ParserException, IOException {
		parserTest(testDir + "/" + "interfaces.cs");
	}

	public void testnamespaces() throws ParserException, IOException {
		parserTest(testDir + "/" + "namespaces.cs");
	}

	public void testsimpleusing() throws ParserException, IOException {
		parserTest(testDir + "/" + "simpleusing.cs");
	}

	// Mono Tests

	public void testMonoa() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/a.cs");
	}

	public void testMonoackermann() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/ackermann.cs");
	}

	public void testMonoappdomainclient() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/appdomain-client.cs");
	}

	public void testMonoappdomain() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/appdomain.cs");
	}

	public void testMonoarraycast() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/array-cast.cs");
	}

	public void testMonoarrayinit() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/array-init.cs");
	}

	public void testMonoarrayinvoke() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/array-invoke.cs");
	}

	public void testMonoarrayvt() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/array-vt.cs");
	}

	public void testMonoarray() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/array.cs");
	}

	public void testMonoarray2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/array2.cs");
	}

	public void testMonoarraylistclone() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/arraylist-clone.cs");
	}

	public void testMonoarraylist() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/arraylist.cs");
	}

	public void testMonoassignabletests() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/assignable-tests.cs");
	}

	public void testMonoautoresetevents() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/autoresetevents.cs");
	}

	public void testMonobench1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/bench1.cs");
	}

	public void testMonobound() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/bound.cs");
	}

	public void testMonobox() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/box.cs");
	}

	public void testMonoc1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/c1.cs");
	}

	public void testMonoc2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/c2.cs");
	}

	public void testMonocasts() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/casts.cs");
	}

	public void testMonocattrcompile() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/cattr-compile.cs");
	}

	public void testMonocharisnumber() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/char-isnumber.cs");
	}

	public void testMonoco1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/co1.cs");
	}

	public void testMonocodegeninterfaces()
		throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/codegen-interfaces.cs");
	}

	public void testMonocodegen() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/codegen.cs");
	}

	public void testMonocodegen2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/codegen2.cs");
	}

	public void testMonoconsole() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/console.cs");
	}

	public void testMonocreateinstance() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/create-instance.cs");
	}

	public void testMonocs1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/cs1.cs");
	}

	public void testMonocustomattr() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/custom-attr.cs");
	}

	public void testMonodecimal() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/decimal.cs");
	}

	public void testMonodelegate() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/delegate.cs");
	}

	public void testMonodelegate1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/delegate1.cs");
	}

	public void testMonodelegate2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/delegate2.cs");
	}

	public void testMonodelegate3() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/delegate3.cs");
	}

	public void testMonodelegate4() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/delegate4.cs");
	}

	public void testMonodelegate5() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/delegate5.cs");
	}

	public void testMonodoublecast() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/double-cast.cs");
	}

	public void testMonoenum() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/enum.cs");
	}

	public void testMonoenum2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/enum2.cs");
	}

	public void testMonoenum3() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/enum3.cs");
	}

	public void testMonoenumcast() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/enumcast.cs");
	}

	public void testMonoexception() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/exception.cs");
	}

	public void testMonoexception10() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/exception10.cs");
	}

	public void testMonoexception11() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/exception11.cs");
	}

	public void testMonoexception12() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/exception12.cs");
	}

	public void testMonoexception13() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/exception13.cs");
	}

	public void testMonoexception14() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/exception14.cs");
	}

	public void testMonoexception2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/exception2.cs");
	}

	public void testMonoexception3() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/exception3.cs");
	}

	public void testMonoexception4() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/exception4.cs");
	}

	public void testMonoexception5() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/exception5.cs");
	}

	public void testMonoexception6() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/exception6.cs");
	}

	public void testMonoexception7() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/exception7.cs");
	}

	public void testMonoexception8() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/exception8.cs");
	}

	public void testMonofib() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/fib.cs");
	}

	public void testMonofieldlayout() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/field-layout.cs");
	}

	public void testMonogencasttest() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/gen-cast-test.cs");
	}

	public void testMonogencheck() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/gen-check.cs");
	}

	public void testMonohashtable() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/hash-table.cs");
	}

	public void testMonohashcode() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/hashcode.cs");
	}

	public void testMonohelloworld() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/helloworld.cs");
	}

	public void testMonoirecursive() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/i-recursive.cs");
	}

	public void testMonoithree() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/i-three.cs");
	}

	public void testMonoiundefined() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/i-undefined.cs");
	}

	public void testMonoi1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/i1.cs");
	}

	public void testMonoi2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/i2.cs");
	}

	public void testMonoi3() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/i3.cs");
	}

	public void testMonoi4() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/i4.cs");
	}

	public void testMonoi5() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/i5.cs");
	}

	public void testMonoi6() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/i6.cs");
	}

	public void testMonoiface() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/iface.cs");
	}

	public void testMonoiface2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/iface2.cs");
	}

	public void testMonoiface3() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/iface3.cs");
	}

	public void testMonoiface4() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/iface4.cs");
	}

	public void testMonoiface6() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/iface6.cs");
	}

	public void testMonoinctest() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/inctest.cs");
	}

	public void testMonoindexer() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/indexer.cs");
	}

	public void testMonointerface() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/interface.cs");
	}

	public void testMonointerface1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/interface1.cs");
	}

	public void testMonointerfaces() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/interfaces.cs");
	}

	public void testMonointptrcast() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/intptrcast.cs");
	}

	public void testMonoinvoke() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/invoke.cs");
	}

	public void testMonoipaddress() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/ipaddress.cs");
	}

	public void testMonoisvaluetype() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/isvaluetype.cs");
	}

	public void testMonoix1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/ix1.cs");
	}

	public void testMonoix2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/ix2.cs");
	}

	public void testMonojitfloat() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/jit-float.cs");
	}

	public void testMonojitint() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/jit-int.cs");
	}

	public void testMonojitlong() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/jit-long.cs");
	}

	public void testMonojituint() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/jit-uint.cs");
	}

	public void testMonojitulong() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/jit-ulong.cs");
	}

	public void testMonolargeexp() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/largeexp.cs");
	}

	public void testMonolargeexp2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/largeexp2.cs");
	}

	public void testMonolong() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/long.cs");
	}

	public void testMonomanualresetevents()
		throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/manualresetevents.cs");
	}

	public void testMonomanylocals() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/many-locals.cs");
	}

	public void testMonomarshal1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/marshal1.cs");
	}

	public void testMonomarshalbyref1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/marshalbyref1.cs");
	}

	public void testMonomis() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/mis.cs");
	}

	public void testMonomutexes() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/mutexes.cs");
	}

	public void testMonon1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/n1.cs");
	}

	public void testMonon2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/n2.cs");
	}

	public void testMononestedloops() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/nested-loops.cs");
	}

	public void testMononewobjvaluetype() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/newobj-valuetype.cs");
	}

	public void testMonononvirt() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/nonvirt.cs");
	}

	public void testMonoobj() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/obj.cs");
	}

	public void testMonooutparm() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/outparm.cs");
	}

	public void testMonoparams() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/params.cs");
	}

	public void testMonopinvoke() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/pinvoke.cs");
	}

	public void testMonopinvoke1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/pinvoke1.cs");
	}

	public void testMonopinvoke2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/pinvoke2.cs");
	}

	public void testMonopinvoke3() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/pinvoke3.cs");
	}

	public void testMonopinvoke4() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/pinvoke4.cs");
	}

	public void testMonopinvoke5() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/pinvoke5.cs");
	}

	public void testMonopinvoke6() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/pinvoke6.cs");
	}

	public void testMonopinvoke7() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/pinvoke7.cs");
	}

	public void testMonopop() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/pop.cs");
	}

	public void testMonoproperty() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/property.cs");
	}

	public void testMonorandom() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/random.cs");
	}

	public void testMonoreflectionenum() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/reflection-enum.cs");
	}

	public void testMonoreflectionprop() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/reflection-prop.cs");
	}

	public void testMonoreflection() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/reflection.cs");
	}

	public void testMonoreflection4() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/reflection4.cs");
	}

	public void testMonoreflection5() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/reflection5.cs");
	}

	public void testMonoremoting1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/remoting1.cs");
	}

	public void testMonoremoting2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/remoting2.cs");
	}

	public void testMonoremoting3() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/remoting3.cs");
	}

	public void testMonorounding() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/rounding.cs");
	}

	public void testMonos1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/s1.cs");
	}

	public void testMonosetenv() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/setenv.cs");
	}

	public void testMonoshift() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/shift.cs");
	}

	public void testMonosieve() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/sieve.cs");
	}

	public void testMonosimpleusing() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/simpleusing.cs");
	}

	public void testMonostaticconstructor()
		throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/static-constructor.cs");
	}

	public void testMonostaticctor() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/static-ctor.cs");
	}

	public void testMonostreamwriter() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/stream-writer.cs");
	}

	public void testMonostream() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/stream.cs");
	}

	public void testMonostringcompare() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/string-compare.cs");
	}

	public void testMonostring() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/string.cs");
	}

	public void testMonostringbuilder() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/stringbuilder.cs");
	}

	public void testMonostruct() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/struct.cs");
	}

	public void testMonoswitchstring() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/switch-string.cs");
	}

	public void testMonoswitch() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/switch.cs");
	}

	public void testMonotest1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-1.cs");
	}

	public void testMonotest10() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-10.cs");
	}

	public void testMonotest100() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-100.cs");
	}

	public void testMonotest101() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-101.cs");
	}

	public void testMonotest102() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-102.cs");
	}

	public void testMonotest103() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-103.cs");
	}

	public void testMonotest104() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-104.cs");
	}

	public void testMonotest105() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-105.cs");
	}

	public void testMonotest106() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-106.cs");
	}

	public void testMonotest107() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-107.cs");
	}

	public void testMonotest108() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-108.cs");
	}

	public void testMonotest109() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-109.cs");
	}

	public void testMonotest11() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-11.cs");
	}

	public void testMonotest110() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-110.cs");
	}

	public void testMonotest111() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-111.cs");
	}

	public void testMonotest112() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-112.cs");
	}

	public void testMonotest113() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-113.cs");
	}

	public void testMonotest114() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-114.cs");
	}

	public void testMonotest115() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-115.cs");
	}

	public void testMonotest116() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-116.cs");
	}

	public void testMonotest117() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-117.cs");
	}

	public void testMonotest118() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-118.cs");
	}

	public void testMonotest119() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-119.cs");
	}

	public void testMonotest12() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-12.cs");
	}

	public void testMonotest120() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-120.cs");
	}

	public void testMonotest121() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-121.cs");
	}

	public void testMonotest122() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-122.cs");
	}

	public void testMonotest123() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-123.cs");
	}

	public void testMonotest124() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-124.cs");
	}

	public void testMonotest125() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-125.cs");
	}

	public void testMonotest126() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-126.cs");
	}

	public void testMonotest127() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-127.cs");
	}

	public void testMonotest128() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-128.cs");
	}

	public void testMonotest129() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-129.cs");
	}

	public void testMonotest13() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-13.cs");
	}

	public void testMonotest130() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-130.cs");
	}

	public void testMonotest131() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-131.cs");
	}

	public void testMonotest132() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-132.cs");
	}

	public void testMonotest133() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-133.cs");
	}

	public void testMonotest134() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-134.cs");
	}

	public void testMonotest135() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-135.cs");
	}

	public void testMonotest136() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-136.cs");
	}

	public void testMonotest137() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-137.cs");
	}

	public void testMonotest138() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-138.cs");
	}

	public void testMonotest139() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-139.cs");
	}

	public void testMonotest14() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-14.cs");
	}

	public void testMonotest140() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-140.cs");
	}

	public void testMonotest141() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-141.cs");
	}

	public void testMonotest142() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-142.cs");
	}

	public void testMonotest143() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-143.cs");
	}

	public void testMonotest144() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-144.cs");
	}

	public void testMonotest145() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-145.cs");
	}

	public void testMonotest146() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-146.cs");
	}

	public void testMonotest147() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-147.cs");
	}

	public void testMonotest148() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-148.cs");
	}

	public void testMonotest149() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-149.cs");
	}

	public void testMonotest15() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-15.cs");
	}

	public void testMonotest150() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-150.cs");
	}

	public void testMonotest151() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-151.cs");
	}

	public void testMonotest152() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-152.cs");
	}

	public void testMonotest16() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-16.cs");
	}

	public void testMonotest17() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-17.cs");
	}

	public void testMonotest18() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-18.cs");
	}

	public void testMonotest19() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-19.cs");
	}

	public void testMonotest2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-2.cs");
	}

	public void testMonotest20() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-20.cs");
	}

	public void testMonotest21() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-21.cs");
	}

	public void testMonotest22() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-22.cs");
	}

	public void testMonotest23() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-23.cs");
	}

	public void testMonotest24() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-24.cs");
	}

	public void testMonotest25() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-25.cs");
	}

	public void testMonotest26() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-26.cs");
	}

	public void testMonotest27() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-27.cs");
	}

	public void testMonotest28() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-28.cs");
	}

	public void testMonotest29() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-29.cs");
	}

	public void testMonotest3() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-3.cs");
	}

	public void testMonotest30() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-30.cs");
	}

	public void testMonotest31() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-31.cs");
	}

	public void testMonotest32() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-32.cs");
	}

	public void testMonotest33() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-33.cs");
	}

	public void testMonotest34() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-34.cs");
	}

	public void testMonotest35() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-35.cs");
	}

	public void testMonotest36() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-36.cs");
	}

	public void testMonotest37() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-37.cs");
	}

	public void testMonotest38() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-38.cs");
	}

	public void testMonotest39() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-39.cs");
	}

	public void testMonotest4() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-4.cs");
	}

	public void testMonotest40() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-40.cs");
	}

	public void testMonotest41() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-41.cs");
	}

	public void testMonotest42() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-42.cs");
	}

	public void testMonotest43() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-43.cs");
	}

	public void testMonotest44() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-44.cs");
	}

	public void testMonotest45() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-45.cs");
	}

	public void testMonotest46() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-46.cs");
	}

	public void testMonotest47() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-47.cs");
	}

	public void testMonotest48() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-48.cs");
	}

	public void testMonotest49() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-49.cs");
	}

	public void testMonotest5() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-5.cs");
	}

	public void testMonotest50() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-50.cs");
	}

	public void testMonotest51() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-51.cs");
	}

	public void testMonotest52() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-52.cs");
	}

	public void testMonotest53() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-53.cs");
	}

	public void testMonotest54() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-54.cs");
	}

	public void testMonotest55() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-55.cs");
	}

	public void testMonotest56() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-56.cs");
	}

	public void testMonotest57() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-57.cs");
	}

	public void testMonotest59() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-59.cs");
	}

	public void testMonotest6() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-6.cs");
	}

	public void testMonotest61() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-61.cs");
	}

	public void testMonotest62() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-62.cs");
	}

	public void testMonotest63() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-63.cs");
	}

	public void testMonotest64() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-64.cs");
	}

	public void testMonotest65() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-65.cs");
	}

	public void testMonotest66() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-66.cs");
	}

	public void testMonotest67() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-67.cs");
	}

	public void testMonotest68() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-68.cs");
	}

	public void testMonotest69() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-69.cs");
	}

	public void testMonotest7() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-7.cs");
	}

	public void testMonotest70() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-70.cs");
	}

	public void testMonotest71() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-71.cs");
	}

	public void testMonotest72() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-72.cs");
	}

	public void testMonotest73() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-73.cs");
	}

	public void testMonotest75() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-75.cs");
	}

	public void testMonotest76() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-76.cs");
	}

	public void testMonotest77() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-77.cs");
	}

	public void testMonotest78() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-78.cs");
	}

	public void testMonotest79() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-79.cs");
	}

	public void testMonotest8() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-8.cs");
	}

	public void testMonotest80() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-80.cs");
	}

	public void testMonotest81() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-81.cs");
	}

	public void testMonotest82() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-82.cs");
	}

	public void testMonotest83() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-83.cs");
	}

	public void testMonotest84() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-84.cs");
	}

	public void testMonotest85() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-85.cs");
	}

	public void testMonotest86() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-86.cs");
	}

	public void testMonotest87() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-87.cs");
	}

	public void testMonotest88() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-88.cs");
	}

	public void testMonotest89() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-89.cs");
	}

	public void testMonotest9() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-9.cs");
	}

	public void testMonotest90() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-90.cs");
	}

	public void testMonotest91() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-91.cs");
	}

	public void testMonotest92() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-92.cs");
	}

	public void testMonotest93() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-93.cs");
	}

	public void testMonotest94() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-94.cs");
	}

	public void testMonotest95() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-95.cs");
	}

	public void testMonotest96() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-96.cs");
	}

	public void testMonotest97() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-97.cs");
	}

	public void testMonotest98() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-98.cs");
	}

	public void testMonotest99() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-99.cs");
	}

	public void testMonotestops() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-ops.cs");
	}

	public void testMonotestprime() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/test-prime.cs");
	}

	public void testMonothread() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/thread.cs");
	}

	public void testMonothread2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/thread2.cs");
	}

	public void testMonothread3() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/thread3.cs");
	}

	public void testMonotightloop() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/tight-loop.cs");
	}

	public void testMonotime() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/time.cs");
	}

	public void testMonotry() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/try.cs");
	}

	public void testMonounreachablecode() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/unreachable-code.cs");
	}

	public void testMonovaluetypegettype()
		throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/valuetype-gettype.cs");
	}

	public void testMonoverify1() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/verify-1.cs");
	}

	public void testMonoverify2() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/verify-2.cs");
	}

	public void testMonovirtualmethod() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/virtual-method.cs");
	}

	public void testMonovtype() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/vtype.cs");
	}

	public void testMonoxtest38() throws ParserException, IOException {
		parserTest(testDir + "/" + "Mono/xtest-38.cs");
	}

}
