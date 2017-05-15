/**
 * util method class
 *
 * @author Dennis Thanner
 * @version 0.0.1
 */
export class Utils {

  /**
   * array compare method
   * @param a
   * @param b
   * @returns {boolean}
   */
  static arraysEqual(a, b) {
    if (a === b) return true;
    if (a == null || b == null) return false;
    if (a.length != b.length) return false;

    // If you don't care about the order of the elements inside
    // the array, you should sort both arrays here.

    for (var i = 0; i < a.length; ++i) {
      if (a[i] !== b[i]) return false;
    }
    return true;
  }
}
