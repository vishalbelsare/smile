/*
 * Copyright (c) 2010-2024 Haifeng Li. All rights reserved.
 *
 * Smile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Smile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Smile.  If not, see <https://www.gnu.org/licenses/>.
 */
 package smile.util;

 import org.junit.jupiter.api.*;
 import static org.junit.jupiter.api.Assertions.*;
 
 /**
  *
  * @author Karl Li
  */
 public class PairingHeapTest {
     public PairingHeapTest() {
     }
 
     @BeforeAll
     public static void setUpClass() throws Exception {
     }
 
     @AfterAll
     public static void tearDownClass() throws Exception {
     }
 
     @BeforeEach
     public void setUp() {
     }
 
     @AfterEach
     public void tearDown() {
     }
 
     @Test
     public void test() {
         System.out.println("PairingHeap");

         PairingHeap<Integer> heap = new PairingHeap<>();
         heap.add(Integer.MAX_VALUE);
         heap.add(10);
         heap.add(7);
         heap.add(5);
         heap.add(4);
         heap.add(9);
         heap.add(2);
         heap.add(3);
         heap.add(2); // add 2 again
         heap.add(1);
         heap.add(0);
         heap.add(-191);
         heap.add(Integer.MIN_VALUE);
         heap.add(6);
         heap.add(Integer.MAX_VALUE);

         assertEquals(Integer.MIN_VALUE, heap.poll());
         assertEquals(-191, heap.poll());
         assertEquals(0, heap.poll());
         assertEquals(1, heap.poll());

         heap.updatePriorities();
         assertEquals(2, heap.poll());
         assertEquals(2, heap.poll());
         assertEquals(3, heap.poll());

         var node = heap.addNode(12);
         node.decrease(0);
         assertEquals(0, heap.peek());
     }
 }
 